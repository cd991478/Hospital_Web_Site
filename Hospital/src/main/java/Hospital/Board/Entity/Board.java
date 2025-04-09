package Hospital.Board.Entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor //(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) /* JPA에게 해당 Entity는 Auditiong 기능을 사용함을 알립니다. */
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(columnDefinition = "varchar(10)", nullable = false)
    private String Author;

    @Column(columnDefinition="varchar(20)", nullable = false)
    private String Title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String Content;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime CreatedDate;

    @LastModifiedDate
    private LocalDateTime ModifiedDate;

    @Builder
    public Board(Integer Id, String Author, String Title, String Content) {
        this.Id = Id;
        this.Author = Author;
        this.Title = Title;
        this.Content = Content;
    }
}
