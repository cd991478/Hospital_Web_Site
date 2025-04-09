package Hospital.D_Hospital.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Hospital.D_Hospital.DTO.D_HospitalListDTO;
import Hospital.D_Hospital.DTO.D_HospitalReadDTO;
import Hospital.D_Hospital.Entity.D_Hospital;
import Hospital.D_Hospital.Entity.D_HospitalRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class D_HospitalService {
	@Autowired
	private D_HospitalRepository d_hr;
	
	public List<D_Hospital> findAll(){
		return d_hr.findAll();
	}
	
	public D_HospitalReadDTO d_hrRead(Integer id) throws NoSuchElementException{
		D_Hospital hospital = this.d_hr.findById(id).orElseThrow();
		return D_HospitalReadDTO.D_HospitalFactory(hospital);
	}
	
	public BigDecimal distanceFromEachOther(BigDecimal a, BigDecimal b, BigDecimal c, BigDecimal d) {
		BigDecimal a_c = a.subtract(c);
		BigDecimal b_d = b.subtract(d);
		BigDecimal a_c_2 = a_c.multiply(a_c);
		BigDecimal b_d_2 = b_d.multiply(b_d);
		BigDecimal ac_bd = a_c_2.add(b_d_2);
		MathContext mc = new MathContext(8);
		BigDecimal ac_bd_root = ac_bd.sqrt(mc);
		return ac_bd_root;
	}
	
	public List<D_Hospital> findAll(HttpServletRequest request){
        HttpSession session = request.getSession();
        String sessionAddress1 = (String) session.getAttribute("P_Address1");
        String sessionAddress2 = (String) session.getAttribute("P_Address2");
        BigDecimal sessionGeoLatitude = (BigDecimal) session.getAttribute("GeoAddressLatitude");
        BigDecimal sessionGeoLongitude = (BigDecimal) session.getAttribute("GeoAddressLongitude");
        System.out.println(sessionAddress1);
        List<D_Hospital> allHospitals = d_hr.findAll();
        HashMap<D_Hospital, BigDecimal> hospitalWithDistance = new HashMap<>();

		for(D_Hospital hospital: allHospitals) {
			if(hospital.getH_Address().contains(sessionAddress1)) {
				if(hospital.getH_Address().contains(sessionAddress2)){ 
					BigDecimal hospitalLatitude = hospital.getH_Latitude(); 
					BigDecimal hospitalLongitude = hospital.getH_Longitude();
					hospitalWithDistance.put(hospital,
					distanceFromEachOther(sessionGeoLatitude, sessionGeoLongitude, 
										  hospitalLatitude,hospitalLongitude)); 
				} 
			} 
		}
		ArrayList<BigDecimal> al = new ArrayList<>(hospitalWithDistance.values());
		ArrayList<D_Hospital> h = new ArrayList<>(hospitalWithDistance.keySet());
		ArrayList<D_Hospital> filteredHospital_v1 = new ArrayList<>();
		Collections.sort(al);
		for(int i = 0; i < al.size(); i++) {
			  for(int j = 0; j < h.size(); j++) {
				  if(hospitalWithDistance.get(h.get(j)) == al.get(i)) {
					  filteredHospital_v1.add(h.get(j));
					  System.out.println(al.get(i));
				  }
			  }
		}
		return filteredHospital_v1;
   }

	public List<D_HospitalListDTO> D_HospitalListSearchPage(Integer page, Integer maxsize, String H_Categorie, String H_Region) {
	    // 페이지 번호가 null이면 첫 페이지로 기본값 설정
	    if (page == null || page < 0) {
	        page = 0;
	    }
	    // 전체 데이터 조회
	    List<D_Hospital> allhospital = this.d_hr.findAll();  // 전체 데이터 조회
	    List<D_Hospital> filteredhospital = allhospital.stream()
	                                                .filter(hospital -> hospital.getH_Region().equals(H_Region) && hospital.getH_Categorie().contains(H_Categorie))
	                                                .collect(Collectors.toList());
	    // 전체 데이터 개수
	    long totalElements = filteredhospital.size();
	    // 전체 페이지 수 계산
	    int totalPages = (int) Math.ceil((double) totalElements / maxsize);
	    // 페이지네이션: 현재 페이지의 시작 인덱스 계산
	    int startIndex = page * maxsize;
	    int endIndex = Math.min(startIndex + maxsize, filteredhospital.size());
	    // 현재 페이지에 해당하는 데이터만 잘라서 가져오기
	    List<D_Hospital> pagedhospital = filteredhospital.subList(startIndex, endIndex);
	    // DTO로 변환하여 반환
	    return pagedhospital.stream()
	                         .map(hospital -> new D_HospitalListDTO(hospital.getH_Id(),
	                        		 hospital.getH_Region(),
	                        		 hospital.getH_Name(),
	                        		 hospital.getH_Department(),
	                        		 hospital.getH_Categorie(),
	                        		 hospital.getBed_Total(),
	                        		 hospital.getBed_General(),
	                        		 hospital.getBed_Psy(),
	                        		 hospital.getBed_Nursing(),
	                        		 hospital.getBed_Intensive(),
	                        		 hospital.getBed_Isolation(),
	                        		 hospital.getBed_Clean(),
	                        		 hospital.getH_Phone_Number(),
	                        		 hospital.getH_Address(),
	                         		 hospital.getH_Latitude(),
	                         		 hospital.getH_Longitude()))
	                         .collect(Collectors.toList());
	}
	
	
	
	public D_HospitalReadDTO Save(D_HospitalReadDTO d_hrDTO) {
		D_Hospital h = ConvertToEntity(d_hrDTO);
		D_Hospital saveh = d_hr.save(h);
		return ConvertToDTO(saveh);
	}
	
	private D_HospitalReadDTO ConvertToDTO(D_Hospital h) {
		D_HospitalReadDTO d_hrDTO = new D_HospitalReadDTO();
		d_hrDTO.setH_Id(h.getH_Id());
		d_hrDTO.setH_Name(h.getH_Name());
		d_hrDTO.setH_Department(h.getH_Department());
		d_hrDTO.setH_Categorie(h.getH_Categorie());
		d_hrDTO.setBed_Total(h.getBed_Total());
		d_hrDTO.setBed_General(h.getBed_General());
		d_hrDTO.setBed_Psy(h.getBed_Psy());
		d_hrDTO.setBed_Nursing(h.getBed_Nursing());
		d_hrDTO.setBed_Clean(h.getBed_Clean());
		d_hrDTO.setBed_Intensive(h.getBed_Intensive());
		d_hrDTO.setBed_Isolation(h.getBed_Isolation());
		d_hrDTO.setBed_Clean(h.getBed_Clean());
		d_hrDTO.setH_Phone_Number(h.getH_Phone_Number());
		d_hrDTO.setH_Address(h.getH_Address());
		d_hrDTO.setH_Latitude(h.getH_Latitude());
		d_hrDTO.setH_Longitude(h.getH_Longitude());
		return d_hrDTO;
	}
	
	private D_Hospital ConvertToEntity(D_HospitalReadDTO d_hrDTO) {
		D_Hospital h = new D_Hospital();
		h.setH_Id(h.getH_Id());
		h.setH_Name(h.getH_Name());
		h.setH_Department(h.getH_Department());
		h.setH_Categorie(h.getH_Categorie());
		h.setBed_Total(h.getBed_Total());
		h.setBed_General(h.getBed_General());
		h.setBed_Psy(h.getBed_Psy());
		h.setBed_Nursing(h.getBed_Nursing());
		h.setBed_Clean(h.getBed_Clean());
		h.setBed_Intensive(h.getBed_Intensive());
		h.setBed_Isolation(h.getBed_Isolation());
		h.setBed_Clean(h.getBed_Clean());
		h.setH_Phone_Number(h.getH_Phone_Number());
		h.setH_Address(h.getH_Address());
		h.setH_Latitude(h.getH_Latitude());
		h.setH_Longitude(h.getH_Longitude());
		return h;
	}
	
	

	
}