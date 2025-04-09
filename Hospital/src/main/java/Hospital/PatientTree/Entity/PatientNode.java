package Hospital.PatientTree.Entity;
import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.*;


import Hospital.Patient.Entity.Patient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true) 
@JsonInclude(JsonInclude.Include.NON_NULL)  
public class PatientNode implements Serializable {
	
    private static final long serialVersionUID = 1L;

    Patient patient; 

	@JsonProperty("left") 
	public PatientNode left;

	@JsonProperty("right") 
	public PatientNode right;

    @JsonProperty("P_Id")
    public Integer getPId() {
        return patient != null ? patient.getP_Id() : null;
    }

    @JsonProperty("P_Name")
    public String getPName() {
        return patient != null ? patient.getP_Name() : null;
    }

    @JsonProperty("P_Gender")
    public Integer getPGender() {
        return patient != null ? patient.getP_Gender() : null;
    }

    @JsonProperty("P_Phone")
    public String getPPhone() {
        return patient != null ? patient.getP_Phone() : null;
    }

    @JsonProperty("P_Address1")
    public String getPAddress1() {
        return (patient != null ? patient.getP_Address1() : null) ;
           
    }
    
    @JsonProperty("P_Pain")
    public Integer getPPatin() {
    	return patient != null ? patient.getP_Pain() : null;
    }

    @JsonProperty("P_Address2")
    public String getPAddress2() {
        return patient != null ? patient.getP_Address2() : null;
    }
    
    @JsonProperty("P_HighRiskGroup")
    public Integer getPHighRiskGroup() {
    	return patient !=null ? patient.getP_HighRiskGroup() : null;
    }
    
    @JsonProperty("P_TakingPill")
    public Integer getPTakingPill() {
    	return patient != null ? patient.getP_TakingPill() : null;
    }
    
    @JsonProperty("P_Cough")
    public Integer getPCough() {
    	return patient != null ? patient.getP_Cough() :  null;
    }
    
    @JsonProperty("P_Covid19")
    public Integer getPCovid19() {
    	return patient != null ? patient.getP_Covid19() : null;
    }
    
    @JsonProperty("P_Nose")
    public Integer getPNose() {
    	return patient != null ? patient.getP_Nose() : null;
    }
    
    @JsonProperty("P_Diarrhea")
    public Integer getPDiarrhea() {
    	return patient != null ? patient.getP_Diarrhea() : null;
    }
    
    @JsonProperty("P_VAS")
    public Integer getPVas() {
    	return patient != null ? patient.getP_VAS() :  null;
    }
    
    @JsonProperty("P_Latitude")
    public BigDecimal getP_Latitude() {
    	return patient != null ? patient.getP_Latitude() : null;
    }
    
    @JsonProperty("P_Longitude")
    public BigDecimal getP_Longitude() {
    	return patient != null ? patient.getP_Longitude() : null;
    }

    public PatientNode(Patient patient) {
        this.patient = patient;
        this.left = null;
        this.right = null;
    }
 
}
