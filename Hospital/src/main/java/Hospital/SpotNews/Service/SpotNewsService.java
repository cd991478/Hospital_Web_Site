package Hospital.SpotNews.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Hospital.SpotNews.DTO.SpotNewsResponseDTO;
import Hospital.SpotNews.Entity.SpotNews;
import Hospital.SpotNews.Entity.SpotNewsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SpotNewsService {
	
	@Autowired
    private SpotNewsRepository snsr;
    
    // 특정 ID로 속보 조회 (GET)
    public Optional<SpotNews> getNewsById(Integer id) {
        return snsr.findById(id);
    }

    // ✅ 특정 ID로 속보 수정 (PUT)
    public Optional<SpotNews> updateNews(Integer id, String title, String content) {
        return snsr.findById(id).map(existingNews -> {
            existingNews.setTitle(title);
            existingNews.setContent(content);
            return snsr.save(existingNews);
        });
    }

    // ✅ 특정 ID로 속보 삭제 (DELETE)
    public boolean deleteNews(Integer id) {
        if (snsr.existsById(id)) {
            snsr.deleteById(id);
            return true;
        }
        return false;
    }
    // 모든 의료 속보 조회 (DTO 변환 적용)
    public List<SpotNewsResponseDTO> getAllNews() {
        return snsr.findTop5ByOrderByPublishedAtDesc()
                .stream()
                .map(SpotNewsResponseDTO::fromEntity) // 엔티티 -> DTO 변환
                .collect(Collectors.toList());
    }
    // 속보 추가 (엔티티 저장 후 DTO 변환하여 반환)
    public SpotNewsResponseDTO addNews(String title, String content) {
        SpotNews news = SpotNews.builder()
                .title(title)
                .content(content)
                .publishedAt(LocalDateTime.now())
                .build();
        SpotNews savedSpotNews = snsr.save(news);
        return SpotNewsResponseDTO.fromEntity(savedSpotNews);
    }
}
