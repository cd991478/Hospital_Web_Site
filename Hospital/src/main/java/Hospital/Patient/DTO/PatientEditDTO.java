package Hospital.Patient.DTO;


import Hospital.Patient.Entity.Patient;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class PatientEditDTO {
	
	@NonNull
	private Integer P_Id;
	@NonNull
	private String P_UserId;
	@NonNull
	private Integer P_Covid19;
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
	// AgreeMent
	
	public Patient Fill(Patient p) {
		p.setP_Name(this.P_Name);
		p.setP_Gender(this.P_Gender);
		p.setP_RegNum(this.P_RegNum);
		p.setP_Phone(this.P_Phone);
		p.setP_Address1(this.P_Address1);
		p.setP_Address2(this.P_Address2);
		p.setP_TakingPill(this.P_TakingPill);
		p.setP_Nose(this.P_Nose);
		p.setP_Cough(this.P_Cough);
		p.setP_Pain(this.P_Pain);
		p.setP_Diarrhea(this.P_Diarrhea);
		p.setP_HighRiskGroup(this.P_HighRiskGroup);
		p.setP_VAS(this.P_VAS);
		p.setP_Covid19(this.P_Covid19);
		return p;
	}
	
	
}
