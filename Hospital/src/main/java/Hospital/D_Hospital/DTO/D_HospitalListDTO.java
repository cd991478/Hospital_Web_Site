package Hospital.D_Hospital.DTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class D_HospitalListDTO { // DB 칼럼과 동일한 명으로 변수명 통일
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
}
