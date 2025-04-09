package Hospital.Appointment.DTO;

import java.time.LocalDateTime;

import Hospital.User.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentListResponseDTO {
	private Integer id;
	private User user;
	private String hospitalname;
	private LocalDateTime createdTime;
	
	public AppointmentListResponseDTO(Integer id, String hospitalname, LocalDateTime createdtime) {
		this.id = id;
		this.hospitalname = hospitalname;
		this.createdTime = createdtime;
	}
	
}
