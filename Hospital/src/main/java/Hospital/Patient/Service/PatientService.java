package Hospital.Patient.Service;





import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import Hospital.Patient.DTO.PatientCreateDTO;
import Hospital.Patient.DTO.PatientEditDTO;
import Hospital.Patient.DTO.PatientEditResponseDTO;
import Hospital.Patient.DTO.PatientListResponseDTO;
import Hospital.Patient.DTO.PatientReadDTO;
import Hospital.Patient.Entity.Patient;
import Hospital.Patient.Entity.PatientRepository;
import Hospital.PatientTree.Service.PatientTreeCacheService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class PatientService {
	   
	   @Autowired
	   private PatientRepository patientRepository;
	   @Autowired
	   private PatientTreeCacheService patientTreeCacheService;
	 

	   public List<Patient> findAll(){
		   return this.patientRepository.findAll();
	   }
	   
	   public Integer PatientInsert(PatientCreateDTO pcDTO) {//PatientService
           Patient patient = Patient.builder()
                       		.P_UserId(pcDTO.getP_UserId())
                             .P_Name(pcDTO.getP_Name())
                             .P_Gender(pcDTO.getP_Gender())
                             .P_RegNum(pcDTO.getP_RegNum())
                             .P_Phone(pcDTO.getP_Phone())
                             .P_Address1(pcDTO.getP_Address1())
                             .P_Address2(pcDTO.getP_Address2())
                             .P_TakingPill(pcDTO.getP_TakingPill())
                             .P_Covid19(pcDTO.getP_Covid19())
                             .P_Nose(pcDTO.getP_Nose())
                             .P_Cough(pcDTO.getP_Cough())
                             .P_Pain(pcDTO.getP_Pain())
                             .P_Diarrhea(pcDTO.getP_Diarrhea())
                             .P_HighRiskGroup(pcDTO.getP_HighRiskGroup())
                             .P_VAS(pcDTO.getP_VAS())
                             .P_Agreement(pcDTO.getP_Agreement())
                             .P_Latitude(pcDTO.getP_Latitude())
                             .P_Longitude(pcDTO.getP_Longitude())
                             .build();
           this.patientRepository.save(patient);
           System.out.println("[DB INSERT] 환자 정보 저장됨: " + patient);
           patientTreeCacheService.addPatientToCache(patient);
           System.out.println("[CACHE UPDATE] 환자 캐시에 저장됨: " + patient);
           return patient.getP_Id();
	   }

	   
	   public PatientReadDTO PatientRead(Integer P_Id, HttpSession session) {
           Patient patient = this.patientRepository.findById(P_Id).orElseThrow();
           
           String P_Address1 = patient.getP_Address1().split(" ")[1];//달서구/수성구/남구 등등
           String P_Address2 = patient.getP_Address1().split(" ")[2];//구마로36길/달구벌대로 등 ~로
           for(int i=0; i < P_Address2.length();i++) {
        	   String modifiedAddress = P_Address2.substring(0, i);
        	   if(modifiedAddress.endsWith("로")) {
        		   P_Address2 = modifiedAddress;
        	   }
           }
           System.out.println(P_Address2);
            session.setAttribute("P_Address1", P_Address1);
            session.setAttribute("P_Address2", P_Address2);
           return PatientReadDTO.PatientInfoFactory(patient);
        }
	   
	   public List<Patient> PatientInfoList(String UserId) {
		   List<Patient> PatientInfoList = new ArrayList<Patient>();
		   List<Patient> patientinfo = this.findAll();
		   for(Patient patient: patientinfo) {
			   if((patient.getP_UserId()!=null) && (patient.getP_UserId().equals(UserId))) {
				   PatientInfoList.add(patient);
			   }
		   }
		   if(PatientInfoList.isEmpty()) {
			   PatientInfoList = null;
		   }
		   return PatientInfoList;
	   }
	 
	   public PatientEditResponseDTO PatientInfoEdit(Integer P_Id) throws NoSuchElementException{
		   	Patient patient = this.patientRepository.findById(P_Id).orElseThrow();
		   	return PatientEditResponseDTO.PatientFactory(patient);
	   }
	   
	   public void PatientInfoUpdate(PatientEditDTO pieDTO) throws NoSuchElementException{
		   Patient patient = this.patientRepository.findById(pieDTO.getP_Id()).orElseThrow();
		   patient = pieDTO.Fill(patient);
		   this.patientRepository.save(patient);
	   }
	   
	   public void PatientInfoDelete(Integer P_Id) {
		   Patient patient = this.patientRepository.findById(P_Id).orElse(null);
		   if(patient != null) {
			   this.patientRepository.deleteById(P_Id);
		   }
	   }
	   
	   public List<PatientListResponseDTO> PatientListPage(Integer page, Integer maxsize, String UserId) {
		    // 페이지 번호가 null이면 첫 페이지로 기본값 설정
		    if (page == null || page < 0) {
		        page = 0;
		    }
		    // 전체 Patient 데이터 조회 (모든 사용자에 대한 Patient 정보)
		    List<Patient> allPatients = this.patientRepository.findAll();  // 전체 데이터 조회

		    // UserId에 맞는 Patient만 필터링
		    List<Patient> filteredPatients = allPatients.stream()
		                                                .filter(patient -> patient.getP_UserId().equals(UserId))  // UserId로 필터링
		                                                .collect(Collectors.toList());

		 // 필터링된 데이터를 날짜를 기준으로 내림차순 정렬
		    filteredPatients.sort((p1, p2) -> p2.getP_InsertDateTime().compareTo(p1.getP_InsertDateTime()));  // 내림차순 정렬
		    // 전체 데이터 개수
		    long totalElements = filteredPatients.size();

		    // 전체 페이지 수 계산
		    int totalPages = (int) Math.ceil((double) totalElements / maxsize);

		    // 페이지네이션: 현재 페이지의 시작 인덱스 계산
		    int startIndex = page * maxsize;
		    int endIndex = Math.min(startIndex + maxsize, filteredPatients.size());

		    // 현재 페이지에 해당하는 데이터만 잘라서 가져오기
		    List<Patient> pagedPatients = filteredPatients.subList(startIndex, endIndex);

		    // DTO로 변환하여 반환
		    return pagedPatients.stream()
		                         .map(patient -> new PatientListResponseDTO(patient.getP_Id(), patient.getP_InsertDateTime()))
		                         .collect(Collectors.toList());
	}
	   
	  
	   
}