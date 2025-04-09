package Hospital.D_Hospital.DTO;


import java.math.BigDecimal;

import Hospital.D_Hospital.Entity.D_Hospital;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class D_HospitalReadDTO { // DB 칼럼과 동일한 명으로 변수명 통일

	private Integer H_Id;
	private String H_Region;
	private String H_Name;
	private String H_Department;
	private String H_Categorie;
	private Integer Bed_Total;
	private Integer Bed_General;
	private Integer Bed_Psy;
	private Integer Bed_Nursing;
	private Integer Bed_Intensive;
	private Integer Bed_Isolation;
	private Integer Bed_Clean;
	private String H_Phone_Number;
	private String H_Address;
	private BigDecimal H_Latitude;
	private BigDecimal H_Longitude;
	
	public D_HospitalReadDTO FromD_Hospital(D_Hospital h) {
		this.H_Id = h.getH_Id();
		this.H_Region = h.getH_Region();
		this.H_Name = h.getH_Name();
		this.H_Department = h.getH_Department();
		this.H_Categorie = h.getH_Categorie();
		this.Bed_Total = h.getBed_Total();
		this.Bed_General = h.getBed_General();
		this.Bed_Psy = h.getBed_Psy();
		this.Bed_Nursing = h.getBed_Nursing();
		this.Bed_Intensive = h.getBed_Intensive();
		this.Bed_Isolation = h.getBed_Isolation();
		this.Bed_Clean = h.getBed_Clean();
		this.H_Phone_Number = h.getH_Phone_Number();
		this.H_Address = h.getH_Address();
		this.H_Latitude = h.getH_Latitude();
		this.H_Longitude = h.getH_Longitude();
		return this;
	}
	
	public static D_HospitalReadDTO D_HospitalFactory(D_Hospital h) {
		D_HospitalReadDTO d_hrDTO = new D_HospitalReadDTO();
		d_hrDTO.FromD_Hospital(h);
		return d_hrDTO;
	}

}