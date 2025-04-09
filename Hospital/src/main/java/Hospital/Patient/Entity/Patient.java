package Hospital.Patient.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "patient")  // MySQL 테이블명 명확히 지정
public class Patient  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Integer P_Id;
    @Column(columnDefinition = "VARCHAR(12) COLLATE utf8mb4_bin")
    private String P_UserId;
    @Column(columnDefinition = "CHAR(6) COLLATE utf8mb4_bin")
    private String P_RegNum;
    @Column(columnDefinition = "CHAR(11) COLLATE utf8mb4_bin")
    private String P_Phone;
    @Column(columnDefinition = "VARCHAR(5)")
    private String P_Name;
    @Column(columnDefinition = "VARCHAR(100)") 
    private String P_Address1;
    @Column(columnDefinition = "VARCHAR(25)")
    private String P_Address2;
    @Column(columnDefinition = "TINYINT")   
    private Integer P_Gender;
    @Column(columnDefinition = "TINYINT")
    private Integer P_Age;
    @Column(columnDefinition = "TINYINT")
    private Integer P_TakingPill;
    @Column(columnDefinition = "TINYINT")
    private Integer P_Covid19;
    @Column(columnDefinition = "TINYINT")
    private Integer P_Nose;
    @Column(columnDefinition = "TINYINT")
    private Integer P_Cough;
    @Column(columnDefinition = "TINYINT")
    private Integer P_Pain;
    @Column(columnDefinition = "TINYINT")
    private Integer P_Diarrhea;
    @Column(columnDefinition = "TINYINT")
    private Integer P_HighRiskGroup;
    @Column(columnDefinition = "TINYINT")
    private Integer P_VAS;
    @Column(columnDefinition = "TINYINT")
    private Integer P_Agreement;
    @Column(columnDefinition = "DECIMAL(15,11)")
    private BigDecimal P_Longitude;
    @Column(columnDefinition = "DECIMAL(15,11)")
    private BigDecimal P_Latitude;
  
    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime P_InsertDateTime;
    

    @PrePersist
    public void generateRandomPId() {
        if (this.P_Id == null) {
            this.P_Id = generateRandomId();
        }
        if (this.P_RegNum != null) {
           this.P_Age = this.CalcAge(this.P_RegNum);
        }
    }
    
    private Integer generateRandomId() {
        Random rand = new Random();
        return rand.nextInt(90000000) + 10000000;
    }
    
    private Integer CalcAge(String RegNum) {
        String StrRegNum = RegNum.toString();
        String StrYear = StrRegNum.substring(0,2);
        String StrMonth = StrRegNum.substring(2,4);
        String StrDay = StrRegNum.substring(4,6);
        int Year = Integer.parseInt(StrYear);
        int Month = Integer.parseInt(StrMonth);
        int Day = Integer.parseInt(StrDay);
        if (Year > LocalDate.now().getYear() % 100) {
             // 2000년대 이후 생일
             Year += 1900;
         } else {Year += 2000;  // 1900년대 생일
         } 
        if(Year <= 99 && Year > LocalDate.now().getYear() % 100) {
           String StrFullYear = "19" + StrYear;
        }
        else {String StrFullYear = "20" + StrYear;}
        LocalDate BirthDate = LocalDate.of(Year, Month, Day);
        LocalDate CurrentDate = LocalDate.now();
        Integer Age = CurrentDate.getYear() - BirthDate.getYear();
        if(CurrentDate.getMonthValue() < BirthDate.getMonthValue() || 
          (CurrentDate.getMonthValue() == BirthDate.getMonthValue() && 
           CurrentDate.getDayOfMonth() < BirthDate.getDayOfMonth())){
           Age--;
        	}
        	return Age;
    }
}