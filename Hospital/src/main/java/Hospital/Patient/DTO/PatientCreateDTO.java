package Hospital.Patient.DTO;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class PatientCreateDTO {
	   @NonNull
	   private String P_UserId;
	   @NonNull
	   private String P_Name;
	   @NonNull
	   private Integer P_Gender;
	   @NonNull
	   private String P_RegNum;
	   @NonNull
	   private String P_Phone;
	   @NonNull
	   private String P_Address1;
	   @NonNull
	   private String P_Address2;
	   @NonNull
	   private Integer P_TakingPill;
	   @NonNull
	   private Integer P_Nose;
	   @NonNull
	   private Integer P_Cough;
	   @NonNull
	   private Integer P_Pain;
	   @NonNull
	   private Integer P_Diarrhea;
	   @NonNull
	   private Integer P_HighRiskGroup;
	   @NonNull
	   private Integer P_VAS;
	   @NonNull
	   private Integer P_Covid19;
	   @NonNull
	   private Integer P_Agreement;
	   @NonNull
	   private Integer P_Age;
	   @NonNull
	   private BigDecimal P_Longitude;
	   @NonNull
	   private BigDecimal P_Latitude;
}
