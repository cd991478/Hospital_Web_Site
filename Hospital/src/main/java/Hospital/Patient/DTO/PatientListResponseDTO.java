package Hospital.Patient.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientListResponseDTO {

	private Integer P_Id;
	private String P_UserId;
	private LocalDateTime P_InsertDateTime;

	
	 public PatientListResponseDTO(Integer p_id, LocalDateTime p_insertdatetime) { 
		 this.P_Id = p_id;
		 this.P_InsertDateTime = p_insertdatetime;
		 }
	 
}
