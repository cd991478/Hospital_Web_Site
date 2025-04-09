package Hospital.Appointment.DTO;

import java.time.LocalDateTime;

import Hospital.Appointment.Entity.Appointment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AppointmentReadDTO {
	  private Integer id; // 예약 ID
	    private String userId; // 사용자 ID
	    private Integer hospitalId; // 병원 ID
	    private String patientName; // 환자 성명
	    private LocalDateTime appointmentTime; // 예약 시간
	    private LocalDateTime createdTime; // 예약 생성 시간
	    
	    public AppointmentReadDTO FromAppointment(Appointment a) {
	    	this.id = a.getId();
	    	this.hospitalId = a.getHospital().getH_Id();
	    	this.patientName = a.getPatientName();
	    	this.appointmentTime = a.getAppointmentTime();
	    	this.createdTime = a.getCreatedTime();
	    	this.userId = a.getUser().getUserId();
	    	return this;
	    }
	    
	    public static AppointmentReadDTO AppointmentFactory(Appointment a) {
	    	AppointmentReadDTO arDTO = new AppointmentReadDTO();
	    	arDTO.FromAppointment(a);
	    	return arDTO;
	    }
}
