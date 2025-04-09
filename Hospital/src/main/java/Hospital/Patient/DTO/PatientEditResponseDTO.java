package Hospital.Patient.DTO;


import java.time.LocalDateTime;

import Hospital.Patient.Entity.Patient;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PatientEditResponseDTO {
	
	private Integer P_Id;
	private String P_UserId;
	private String P_Name;
	private Integer P_Gender;
	private String P_RegNum;
	private String P_Phone;
	private String P_Address1;
	private String P_Address2;
	private Integer P_TakingPill;
	private Integer P_Nose;
	private Integer P_Cough;
	private Integer P_Pain;
	private Integer P_Diarrhea;
	private Integer P_HighRiskGroup;
	private Integer P_VAS;
	private Integer P_Agreement;
	private LocalDateTime P_InsertDateTime;
	private Integer P_Age;
	private Integer P_Covid19;
	
	public PatientEditResponseDTO FromPatient(Patient p) {
		  this.P_Id = p.getP_Id();
		  this.P_UserId = p.getP_UserId();
	      this.P_Name = p.getP_Name();
	      this.P_Gender = p.getP_Gender();
	      this.P_RegNum = p.getP_RegNum();
	      this.P_Phone = p.getP_Phone();
	      this.P_Address1 = p.getP_Address1();
	      this.P_Address2 = p.getP_Address2();
	      this.P_TakingPill = p.getP_TakingPill();
	      this.P_Nose = p.getP_Nose();
	      this.P_Cough = p.getP_Cough();
	      this.P_Pain = p.getP_Pain();
	      this.P_Diarrhea = p.getP_Diarrhea();
	      this.P_HighRiskGroup = p.getP_HighRiskGroup();
	      this.P_VAS = p.getP_VAS();
	      this.P_Agreement = p.getP_Agreement();
	      this.P_InsertDateTime = p.getP_InsertDateTime();
	      this.P_Age = p.getP_Age();
	      this.P_Covid19 = p.getP_Covid19();
	      return this;
	}
	
	public static PatientEditResponseDTO PatientFactory(Patient p) {
		PatientEditResponseDTO pierDTO = new PatientEditResponseDTO();
		pierDTO.FromPatient(p);
		return pierDTO;
	}
}