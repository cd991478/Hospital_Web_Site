package Hospital.SpotNews.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpotNews {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    @Column(columnDefinition = "varchar(50)", nullable = false)
    private String title; // 속보 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 속보 내용

    @Column(nullable = false)
    private LocalDateTime publishedAt; // 속보 게시일

}
