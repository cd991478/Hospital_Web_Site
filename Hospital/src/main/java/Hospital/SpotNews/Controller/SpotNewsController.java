package Hospital.SpotNews.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import Hospital.SpotNews.DTO.SpotNewsRequestDTO;
import Hospital.SpotNews.Entity.SpotNews;
import Hospital.SpotNews.Service.SpotNewsService;

@RestController
@RequestMapping("/Home")  // 모든 요청이 "/Home" 아래에 위치하도록 설정
public class SpotNewsController {
	
	@Autowired
	private  SpotNewsService sns;
	
	//POST 요청을 처리하여 의료 속보 추가
    @PostMapping
    public ResponseEntity<String> addSpotNews(@RequestBody SpotNewsRequestDTO requestDTO) {
        sns.addNews(requestDTO.getTitle(), requestDTO.getContent());
        return ResponseEntity.ok("의료 속보가 성공적으로 추가되었습니다.");
    }
    
    // GET: 특정 ID의 속보 조회
    @GetMapping("/{id}")
    public ResponseEntity<SpotNews> getSpotNews(@PathVariable("id") Integer id) {
        Optional<SpotNews> spotNews = sns.getNewsById(id);
        return spotNews.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // PUT: 특정 ID의 속보 수정
    @PutMapping("/{id}")
    public ResponseEntity<SpotNews> updateSpotNews(@PathVariable("id") Integer id, @RequestBody SpotNewsRequestDTO requestDTO) {
        Optional<SpotNews> updatedNews = sns.updateNews(id, requestDTO.getTitle(), requestDTO.getContent());
        return updatedNews.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // DELETE: 특정 ID의 속보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpotNews(@PathVariable("id") Integer id) {
        if (sns.deleteNews(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
