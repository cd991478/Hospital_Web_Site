package Hospital.Board.DTO;

import java.time.LocalDateTime;

import Hospital.Board.Entity.Board;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class BoardEditDTO {
	
	@NonNull
	private Integer Id;
	
	
	@NonNull			
	private String Title;
	
	@NonNull
	private String Author;
	
	
	@NonNull
	private String Content;
	
	private LocalDateTime ModifiedDate;
	
	public Board Fill(Board b) {
		b.setTitle(this.Title);
		b.setAuthor(this.Author);
		b.setContent(this.Content);
		b.setModifiedDate(this.ModifiedDate);
		return b;
	}
}
