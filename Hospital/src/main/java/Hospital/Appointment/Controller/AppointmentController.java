package Hospital.Appointment.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import Hospital.Appointment.DTO.AppointmentDTO;
import Hospital.Appointment.DTO.AppointmentListResponseDTO;
import Hospital.Appointment.DTO.AppointmentReadDTO;
import Hospital.Appointment.Entity.AppointmentRepository;
import Hospital.Appointment.Service.AppointmentService;
import Hospital.D_Hospital.DTO.D_HospitalListDTO;
import Hospital.D_Hospital.DTO.D_HospitalReadDTO;
import Hospital.D_Hospital.Entity.D_Hospital;
import Hospital.D_Hospital.Entity.D_HospitalRepository;
import Hospital.D_Hospital.Service.D_HospitalService;


import Hospital.Patient.Service.PatientService;
import Hospital.User.DTO.UserReadDTO;
import Hospital.User.Service.UserService;
import jakarta.servlet.http.HttpSession;


@Controller
public class AppointmentController {


	@Autowired
    private D_HospitalRepository d_hr;
    
	@Autowired
    private AppointmentService appointmentService;
	
	@Autowired
	private AppointmentRepository apr;
	@Autowired
	private UserService uls;
	
	@Autowired
	private D_HospitalService d_hs;
	
	@Autowired
	private PatientService pis;
    
    
    
    // 병원 목록을 검색하는 GET 요청 처리
    @GetMapping("/AppointmentPage/Input")
    public ModelAndView searchHospitals(
    		@RequestParam(name="page", required=false) Integer page,
            @RequestParam(value = "H_Categorie", required = false) String H_Categorie,
            @RequestParam(value = "H_Region", required = false) String H_Region,
            												HttpSession session, Model model) {
    	String UserId = (String)session.getAttribute("UserId");
    	final int PageSize = 10;
    	
    	String UserName = (String)session.getAttribute("UserName");
    	
        System.out.println("======== 컨트롤러 진입 ========");
        System.out.println("H_Categorie: " + H_Categorie);
        System.out.println("H_Region: " + H_Region);
        System.out.println(UserId);
        
        if(H_Categorie == null || H_Region == null) {
        	ModelAndView mav = new ModelAndView();
            mav.addObject("D_HospitalList", null);
            mav.addObject("UserName",UserName);
            mav.setViewName("AppointmentPage/Input");
            return mav;
        }
        if (page == null || page < 0) {
	        page = 0;  // 첫 페이지를 기본값으로 설정
	    }
        List<D_HospitalListDTO> HospitalList = this.d_hs.D_HospitalListSearchPage(page, PageSize, H_Categorie, H_Region);
        
        List<D_Hospital> allhospital = this.d_hr.findAll();
        long totalElements = allhospital.stream()
                .filter(hospital -> hospital.getH_Region().equals(H_Region) && hospital.getH_Categorie().contains(H_Categorie))
                .count();
     // 전체 페이지 수 계산
	    int totalPages = (int) Math.ceil((double) totalElements / PageSize);  // 전체 페이지 수 계산
	    int currentPage = page;  // 현재 페이지 번호
	    if(totalElements == 0) {
	    	HospitalList = null;
	 }
        
	    
	    model.addAttribute("D_HospitalList", HospitalList);
	    model.addAttribute("totalPages", totalPages);  // 전체 페이지 수
	    model.addAttribute("totalElements", totalElements);  // 전체 수
	    model.addAttribute("currentPage", currentPage);  // 현재 페이지 번호
	    model.addAttribute("pageSize", PageSize);  // 한 페이지당 개수
	    
	    ModelAndView mav = new ModelAndView();
	    mav.addObject("H_Categorie",(String)H_Categorie);
	    mav.addObject("H_Region",(String)H_Region);
	    mav.setViewName("AppointmentPage/Input");
	    return mav;
	    
    }
    
    @PostMapping("/AppointmentPage/Reserve")
    public String reserveAppointment(@RequestParam("selectedHospitals") Integer hospitalId,// 선택된 병원 ID
                                     @RequestParam(value = "patientName") String patientName,// 환자 성명
                                     @RequestParam(value = "appointmentTime") LocalDateTime appointmentTime,
                                     HttpSession session) {                        
    	String userId = (String) session.getAttribute("UserId");// 사용자 ID
        System.out.println("hospitalId: " + hospitalId);
        System.out.println("patientName: " + patientName);
        System.out.println("appointmentTime: " + appointmentTime);
        System.out.println("userId: " + userId);
        AppointmentDTO appointmentDTO = new AppointmentDTO();// 예약 DTO로 데이터 전달
        appointmentDTO.setHospitalId(hospitalId);  // 병원 ID
        appointmentDTO.setPatientName(patientName); // 환자 성명
        appointmentDTO.setAppointmentTime(appointmentTime); // 예약 시간
        appointmentDTO.setUserId(userId); // 사용자 ID
        AppointmentDTO savedAppointment = appointmentService.createAppointment(appointmentDTO);
        Integer id = savedAppointment.getId(); // 

        return String.format("redirect:/AppointmentPage/Result/%s", id);
    }
    
    @GetMapping("/AppointmentPage/Result/{id}") // 제출버튼시 결과창 화면 이동 함수
	public ModelAndView AppointmentResult(@PathVariable("id") Integer Id) throws NoSuchElementException{
		  ModelAndView mav = new ModelAndView();
		  UserReadDTO uiDTO = new UserReadDTO();
	      AppointmentReadDTO aprDTO = this.appointmentService.AppointmentRead(Id);
	      uiDTO = this.uls.UserInfoRead(aprDTO.getUserId());
	      System.out.println("아이디 "+aprDTO.getUserId());
	      System.out.println("병원아이디 "+aprDTO.getHospitalId());
	      System.out.println("예약아이디 "+aprDTO.getId());
	      System.out.println("예약시간"+aprDTO.getAppointmentTime());
	      System.out.println("시간"+aprDTO.getCreatedTime());
	      Integer id = aprDTO.getHospitalId();
	      D_HospitalReadDTO d_hrDTO = this.d_hs.d_hrRead(id);
	      mav.addObject("hospital", d_hrDTO);
	      mav.addObject("UserInfo",uiDTO);
	      mav.addObject("AppointmentInfo",aprDTO);
	      mav.setViewName("/AppointmentPage/Result");
	      return mav;
	   }
    
    @GetMapping("/AppointmentPage/List") // 예약 목록 이동
    public ModelAndView AppointmentList(@RequestParam(name="page", required=false) Integer page, 
                                        HttpSession session, Model model) {
        final int PageSize = 5; // 한 페이지당 예약 개수
        
        // 세션에서 UserId 가져오기
        String UserId = (String) session.getAttribute("UserId");

        // UserId가 없으면 로그인 페이지로 리디렉트
        if (UserId == null) {
            return new ModelAndView("redirect:/login");
        }

        UserReadDTO uiDTO = this.uls.UserInfoRead(UserId);
        model.addAttribute("UserId", UserId);
        model.addAttribute("UserName", uiDTO.getUserName());

        // 페이지 번호 유효성 검사 및 기본값 설정
        if (page == null || page < 0) {
            page = 0;
        }

        // UserId에 해당하는 예약 목록 가져오기 (페이징 적용)
        List<AppointmentListResponseDTO> AppointmentList = this.appointmentService.AppointmentListPage(page, PageSize, UserId);

        // UserId로 필터링된 예약 목록 개수 구하기
        long totalElements = this.apr.findAll().stream()
                                    .filter(appointment -> appointment.getUser().getUserId().equals(UserId))
                                    .count();

        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalElements / PageSize);
        if (totalElements == 0) {
            page = 0;
            AppointmentList = new ArrayList<>(); // 빈 리스트로 설정
        }

        // 페이지가 총 페이지 수를 초과하면 마지막 페이지로 설정
        if (page >= totalPages && totalPages > 0) {
            page = totalPages - 1;
        }

     // 만약 totalElements가 0이면 AppointmentList를 빈 리스트로 초기화
        if (AppointmentList == null) {
            AppointmentList = new ArrayList<>();
        }

        // 이전, 다음 페이지 설정
        int prevPage = (page > 0) ? page - 1 : 0;
        int nextPage = (page < totalPages - 1) ? page + 1 : totalPages - 1;

        // 만약 예약 데이터가 없으면 빈 리스트 반환
        if (totalElements == 0) {
            AppointmentList = new ArrayList<>();
        }

        // 모델에 페이지네이션 정보 추가
        model.addAttribute("AppointmentList", AppointmentList);
        model.addAttribute("totalPages", totalPages);  
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("currentPage", page);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("pageSize", PageSize);

        // 뷰 반환
        ModelAndView mav = new ModelAndView();
        mav.setViewName("AppointmentPage/List");
        return mav;
    }


    @GetMapping("/AppointmentPage/Delete")
	public String PatientInfoDelete(@RequestParam("Id") Integer Id, HttpSession session, Model model) {
		  this.appointmentService.AppointmentDelete(Id);
		  return "redirect:/AppointmentPage/List";
		 
	}
	
}