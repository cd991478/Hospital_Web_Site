package Hospital.Appointment.Entity;

import java.time.LocalDateTime;

import Hospital.D_Hospital.Entity.D_Hospital;
import Hospital.User.Entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "H_Id", nullable = false)
    private D_Hospital hospital;
    @Column(columnDefinition = "VARCHAR(4)")
    private String patientName; 
    @Column
    private LocalDateTime appointmentTime;
    @Column
    private LocalDateTime createdTime;
}