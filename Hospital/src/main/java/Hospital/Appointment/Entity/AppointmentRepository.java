package Hospital.Appointment.Entity;

import org.springframework.data.jpa.repository.JpaRepository;



public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
	
}