package Hospital.Board.DTO;

import java.time.LocalDateTime;

import Hospital.Board.Entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BoardEditResponseDTO {
	private Integer Id;
	private String Title;
	private String Author;
	private String Content;
	private LocalDateTime CreatedDate;
	private LocalDateTime ModifiedDate;
	
	public BoardEditResponseDTO FromBoard(Board b) {
		this.Id = b.getId();
		this.Title = b.getTitle();
		this.Author = b.getAuthor();
		this.Content = b.getContent();
		this.CreatedDate = b.getCreatedDate();
		this.ModifiedDate = b.getModifiedDate();
		return this; 
	}
	
	public static BoardEditResponseDTO BoardFactory(Board b) {
		BoardEditResponseDTO beDTO = new BoardEditResponseDTO();
		beDTO.FromBoard(b);
		return beDTO;
	}
}

