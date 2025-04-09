package Hospital.D_Hospital.Entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="d_hospital_v1")
public class D_Hospital {
	@Id @Column(unique=true)
	private Integer H_Id;
	@Column(name = "H_Region", columnDefinition="VARCHAR(3)")
	private String H_Region;
	@Column(name = "H_Name", columnDefinition="VARCHAR(30)")
	private String H_Name;
	@Column(name = "H_Department", columnDefinition="VARCHAR(6)")
	private String H_Department;
	@Column(name = "H_Categorie", columnDefinition = "VARCHAR(45)")
	private String H_Categorie;
	@Column(name = "Bed_Total", columnDefinition = "SMALLINT" )
	private Integer Bed_Total;
	@Column(name = "Bed_General", columnDefinition = "SMALLINT")
	private Integer Bed_General;
	@Column(name = "Bed_Psy", columnDefinition = "SMALLINT")
	private Integer Bed_Psy;
	@Column(name = "Bed_Nursing", columnDefinition = "SMALLINT")
	private Integer Bed_Nursing;
	@Column(name = "Bed_Intensive", columnDefinition = "TINYINT")
	private Integer Bed_Intensive;
	@Column(name = "Bed_Isolation", columnDefinition = "TINYINT")
	private Integer Bed_Isolation;
	@Column(name = "Bed_Clean", columnDefinition = "TINYINT")
	private Integer Bed_Clean;
	@Column(name = "H_Phone_Number", columnDefinition = "VARCHAR(14)")
	private String H_Phone_Number;
	@Column(name = "H_Address", columnDefinition = "VARCHAR(100)")
	private String H_Address;
    @Column(name = "H_Latitude", precision = 10, scale = 8)
    private BigDecimal H_Latitude;
    @Column(name = "H_Longitude", precision = 11, scale = 8)
    private BigDecimal H_Longitude;
}
