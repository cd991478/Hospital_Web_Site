package Hospital.Patient.Entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PatientRepository extends JpaRepository<Patient, Integer> {

	 // 🔹 JPQL 방식 (권장)
    @Query("SELECT p FROM Patient p WHERE p.P_Covid19 = 1")
    List<Patient> findByP_Covid19();
}
