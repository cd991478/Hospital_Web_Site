package Hospital.Board.DTO;

import java.time.LocalDateTime;

import Hospital.Board.Entity.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardReadDTO {
	
	private Integer Id;
    private String Author;
    private String Title;
    private String Content;
    private LocalDateTime CreatedDate;
    private LocalDateTime ModifiedDate;


    public BoardReadDTO FromBoard(Board b) {
    	this.Id = b.getId();
    	this.Author = b.getAuthor();
    	this.Title = b.getTitle();
    	this.Content = b.getContent();
    	this.CreatedDate = b.getCreatedDate();
    	this.ModifiedDate = b.getModifiedDate();
    	return this;
    }
    
    public static BoardReadDTO BoardFactory(Board b) {
    	BoardReadDTO brDTO = new BoardReadDTO();
    	brDTO.FromBoard(b);
    	return brDTO;
    	
    }
    
}
