package Hospital.SpotNews.DTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class SpotNewsCreateDTO {
	@NonNull
	@Positive
	private Integer id;
	
	@NonNull
	private String title;
	
	@NonNull
	private String content;
	
	@NonNull
	 private LocalDateTime publishedAt;
}
