package Hospital.Patient.Controller;

import java.math.BigDecimal;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import Hospital.D_Hospital.Entity.D_Hospital;
import Hospital.D_Hospital.Service.D_HospitalService;
import Hospital.Patient.DTO.PatientCreateDTO;
import Hospital.Patient.DTO.PatientEditDTO;
import Hospital.Patient.DTO.PatientEditResponseDTO;
import Hospital.Patient.DTO.PatientListResponseDTO;
import Hospital.Patient.DTO.PatientReadDTO;
import Hospital.Patient.Entity.Patient;
import Hospital.Patient.Entity.PatientRepository;
import Hospital.Patient.Service.PatientService;
import Hospital.PatientTree.Service.PatientTreeCacheService;
import Hospital.User.DTO.UserReadDTO;
import Hospital.User.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import Hospital.Patient.Service.PatientGeographyService;

@Controller
@SessionAttributes("UserId") 
public class PatientController {

	@Autowired
	private PatientRepository pir;
	@Autowired
	private PatientService pis;
	@Autowired
	private UserService uis;
	@Autowired
	private D_HospitalService d_hs;
	@Autowired
	private PatientTreeCacheService ptcs;
	@Autowired
	private PatientGeographyService pgs;
	
	String[] p_address = new String[5];// 도로명 주소의 체계에 따라 5개의 공백 구분에 맞게 배열 크기 선정
	String address1; // DB에서 가져온 주소
	String address2;
	List<D_Hospital> D_HospitalList = new ArrayList<>();

	@GetMapping("/PatientInfo/Input") // 문진표 입력 받는 화면 출력 실행 함수
	public ModelAndView PatientInfoInput(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		UserReadDTO uiDTO = new UserReadDTO();
		String UserId = (String) session.getAttribute("UserId");
		System.out.println(UserId);
		uiDTO = this.uis.UserInfoRead(UserId);
		mav.addObject("UserInfo", uiDTO);
		mav.setViewName("/PatientInfo/Input");
		return mav;
	}

	@PostMapping("/PatientInfo/Input")
	public String PatientInfoInsert(PatientCreateDTO pcDTO, HttpSession session) {
		//세션에서 UserId 가져오기 (이 부분은 유지해야 함)
		String P_UserId = (String) session.getAttribute("UserId");
		pcDTO.setP_UserId(P_UserId);
		 BigDecimal[] coordinates = pgs.getCoordinates(pcDTO.getP_Address1());
			if (coordinates != null) {
				System.out.println("[GEO] 주소 변환 성공: " + 
						pcDTO.getP_Address1() + " → 위도: " + coordinates[0] + ", 경도: "
						+ coordinates[1]);
			} else {
				System.out.println("[GEO] 주소 변환 실패: " + pcDTO.getP_Address1());
			}
			pcDTO.setP_Latitude(coordinates[0]);//위도
			pcDTO.setP_Longitude(coordinates[1]);//경도
		//환자 정보 저장
		Integer P_Id = this.pis.PatientInsert(pcDTO);
		session.setAttribute("P_Id", P_Id); // 세션에 P_Id 저장
		//  트리를 캐싱 서비스에서 관리
		Patient patient = this.pir.findById(P_Id).orElse(null);
		if (patient != null) {
			ptcs.addPatientToCache(patient); // 캐싱된 트리 업데이트
			System.out.println("[CACHE UPDATE] 트리에 새로운 환자 추가됨: " + patient.getP_Name());
		} else {
			System.out.println("환자 정보가 없어 트리에 추가하지 못함.");
		}
		return "redirect:/PatientInfo/Result/" + P_Id;
	}

	@GetMapping("/PatientInfo/Result/{P_Id}") 
	public ModelAndView PatientInfoResult(@PathVariable("P_Id") Integer P_Id, HttpSession session) throws Exception {
	    ModelAndView mav = new ModelAndView();    
	    if (P_Id == null) {
	        throw new NoSuchElementException("문진표 정보가 없습니다.");
	    }
	    PatientReadDTO pirDTO = this.pis.PatientRead(P_Id, session);
	    String SessionUserId = (String) session.getAttribute("UserId");

	    if (!pirDTO.getP_UserId().equals(SessionUserId)) {
	        throw new SecurityException("잘못된 접근입니다.");
	    }
	    BigDecimal[] coordinates = pgs.getCoordinates(pirDTO.getP_Address1());
		if (coordinates != null) {
			System.out.println("[GEO] 주소 변환 성공: " + 
						pirDTO.getP_Address1() + " → 위도: " + coordinates[0] + ", 경도: "
					+ coordinates[1]);
		} else {
			System.out.println("[GEO] 주소 변환 실패: " + pirDTO.getP_Address1());
		}
		session.setAttribute("GeoAddressLatitude", coordinates[0]);
		session.setAttribute("GeoAddressLongitude", coordinates[1]);
		p_address = pirDTO.getP_Address1().split(" ");
		mav.addObject("PatientData", pirDTO);
		address1 = p_address[1];// 달서구
		address2 = p_address[2];// 구마로
		mav.setViewName("PatientInfo/Result");
		session.setAttribute("P_Id", P_Id);
		return mav;
	}

	@GetMapping("/PatientInfo/Edit") // 결과창에서 수정버튼 누르면 이동하는 함수
	public ModelAndView PatientInfoEdit(HttpSession session) throws NoSuchElementException {
		ModelAndView mav = new ModelAndView();
		Integer P_Id = (Integer) session.getAttribute("P_Id");
		if (P_Id == null) {
			throw new NoSuchElementException("잘못된 접근입니다.");
		}
		PatientEditResponseDTO pierDTO = this.pis.PatientInfoEdit(P_Id);

		mav.addObject("PatientInfoEdit", pierDTO);
		mav.setViewName("PatientInfo/Edit");
		return mav;
	}

	@PostMapping("/PatientInfo/Edit") // 수정 화면에서 완료 버튼 누를시 완료, 이동하는 함수
	public String PatientInfoUpdate(@Validated PatientEditDTO pieDTO, HttpSession session, Errors errors) throws NoSuchAlgorithmException {
	
		Integer P_Id = (Integer) session.getAttribute("P_Id");
		if (P_Id == null) {
			throw new NoSuchElementException("문진표 정보를 찾을 수 없습니다.");
		}
		session.setAttribute("P_Id", P_Id);
		pieDTO.setP_Id(P_Id);
		this.pis.PatientInfoUpdate(pieDTO);
		return "redirect:/PatientInfo/Result/" + P_Id;
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ModelAndView NoSuchElementExceptionHandler(NoSuchElementException ex) {
		return this.Error422("문진표 정보가 없습니다.", "/PatientInfo");
	}

	private ModelAndView Error422(String message, String location) {
		ModelAndView mav = new ModelAndView();
		mav.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
		mav.addObject("Message", message);
		mav.addObject("Location", location);
		mav.setViewName("common/error/422"); // 추후 추가
		return mav;
	}

	@GetMapping("/PatientInfo/HospitalList") // 병원 목록 선별 메서드
	public ModelAndView getAllHospitals(HttpServletRequest request) {
		List<D_Hospital> hospitals = d_hs.findAll(request);
		ModelAndView mav = new ModelAndView("HospitalList");
		mav.addObject("D_HospitalList", hospitals);
		mav.setViewName("PatientInfo/HospitalList");
		return mav;
	}

	@GetMapping("/PatientInfo/List") // 문진표 목록 이동
	public ModelAndView PatientInfoList(@RequestParam(name = "page", required = false) Integer page,
			HttpSession session, Model model) {
		final int PageSize = 5;
		String UserId = (String) session.getAttribute("UserId");
		UserReadDTO uiDTO = this.uis.UserInfoRead(UserId);
		model.addAttribute("UserId", UserId);
		model.addAttribute("UserName", uiDTO.getUserName());

		// 페이지 번호가 null일 경우 0으로 설정
		if (page == null || page < 0) {
			page = 0; // 첫 페이지를 기본값으로 설정
		}

		// UserId에 해당하는 문진표 목록 가져오기
		List<PatientListResponseDTO> PatientInfoList = this.pis.PatientListPage(page, PageSize, UserId);

		// UserId로 필터링된 전체 문진표 개수 구하기 (findAll()으로 전체 데이터 확인 후 필터링)
		List<Patient> allPatients = this.pir.findAll(); // 전체 데이터 조회
		long totalElements = allPatients.stream().filter(patient -> patient.getP_UserId().equals(UserId)) // UserId로 필터링
				.count(); // 필터링된 Patient 목록의 개수

		// 전체 페이지 수 계산
		int totalPages = (int) Math.ceil((double) totalElements / PageSize); // 전체 페이지 수 계산
		int currentPage = page; // 현재 페이지 번호
		if (totalElements == 0) {
			PatientInfoList = null;
		}
		// 모델에 페이지네이션 정보 추가
		model.addAttribute("PatientInfoList", PatientInfoList);
		model.addAttribute("totalPages", totalPages); // 전체 페이지 수
		model.addAttribute("totalElements", totalElements); // 전체 문진표 수
		model.addAttribute("currentPage", currentPage); // 현재 페이지 번호
		model.addAttribute("pageSize", PageSize); // 한 페이지당 문진표 개수

		ModelAndView mav = new ModelAndView();
		mav.setViewName("PatientInfo/List");
		return mav;
	}

	@GetMapping("/PatientInfo/Delete") // 문진표 삭제
	public String PatientInfoDelete(@RequestParam("P_Id") Integer P_Id, Model model) {
		this.pis.PatientInfoDelete(P_Id);
		model.addAttribute("message", "문진표가 삭제되었습니다.");
		return "redirect:/PatientInfo/List";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 세션 초기화
		System.out.println("세션 초기화 완료: 트리 삭제됨");
		return "redirect:/login";
	}


	    
}