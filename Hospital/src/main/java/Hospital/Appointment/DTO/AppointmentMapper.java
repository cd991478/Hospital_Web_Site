package Hospital.Appointment.DTO;

import java.time.LocalDateTime;

import Hospital.Appointment.Entity.Appointment;
import Hospital.D_Hospital.Entity.D_Hospital;
import Hospital.User.Entity.User;

public class AppointmentMapper {

    // 엔터티 → DTO 변환
    public static AppointmentDTO toDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setUserId(appointment.getUser().getUserId());
        dto.setHospitalId(appointment.getHospital().getH_Id());
        dto.setPatientName(appointment.getPatientName());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setCreatedTime(appointment.getCreatedTime());
        return dto;
    }

    // DTO → 엔터티 변환
    public static Appointment toEntity(AppointmentDTO dto, User user, D_Hospital hospital) {
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setHospital(hospital);
        appointment.setPatientName(dto.getPatientName());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setCreatedTime(LocalDateTime.now());
        return appointment;
    }
    
    
}