package Hospital.Appointment.DTO;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentDTO {
    private Integer id; // 예약 ID
    private String userId; // 사용자 ID
    private Integer hospitalId; // 병원 ID
    private String patientName; // 환자 성명
    private LocalDateTime appointmentTime; // 예약 시간
    private LocalDateTime createdTime; // 예약 생성 시간
}