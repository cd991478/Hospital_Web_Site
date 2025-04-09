package Hospital.Board.DTO;

import java.time.LocalDateTime;

import Hospital.Board.Entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDTO {
    private Integer Id;
    private String Author;
    private String Title;
    private String Content;
    private LocalDateTime CreatedDate;
    private LocalDateTime ModifiedDate;

    public Board ToEntity() {
        Board build = Board.builder()
                .Id(Id)
                .Author(Author)
                .Title(Title)
                .Content(Content)
                .build();
        return build;
    }

    @Builder
    public BoardDTO(Integer Id, String Author, String Title, String Content, LocalDateTime CreatedDate, LocalDateTime ModifiedDate) {
        this.Id = Id;
        this.Author = Author;
        this.Title = Title;
        this.Content = Content;
        this.CreatedDate = CreatedDate;
        this.ModifiedDate = ModifiedDate;
    }
}