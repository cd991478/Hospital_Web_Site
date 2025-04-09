package Hospital.SpotNews.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Hospital.SpotNews.Entity.SpotNews;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotNewsResponseDTO {

    private Integer id;
    private String title;
    private String content;
    private String publishedAt; // 날짜를 문자열로 변환 (포맷 적용)

    // 엔티티를 DTO로 변환하는 메서드
    public static SpotNewsResponseDTO fromEntity(SpotNews news) {
        return SpotNewsResponseDTO.builder()
                .id(news.getId())
                .title(news.getTitle())
                .content(news.getContent())
                .publishedAt(formatDate(news.getPublishedAt()))
                .build();
    }

    // 날짜 포맷팅 메서드 (yyyy-MM-dd HH:mm:ss 형식)
    private static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}