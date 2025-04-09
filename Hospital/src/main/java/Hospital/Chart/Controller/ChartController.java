package Hospital.Chart.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import Hospital.Chart.Service.ChartService;
import Hospital.Patient.Entity.Patient;
import Hospital.Patient.Entity.PatientRepository;


@Controller
public class ChartController {
	
	@Autowired
	private PatientRepository pr;
	@Autowired
	private ChartService cs;

	
	// JSON API (데이터 제공 → 경로를 `/Chart/ShowPatientData`로 변경)
		@GetMapping("/Chart/ShowPatientData")
		public ResponseEntity<?> getCovid19Patients() {
		    try {
		        List<Patient> patients = pr.findByP_Covid19();
		        if (patients.isEmpty()) {
		            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No patients found");
		        }
		        return ResponseEntity.ok(patients);
		    } catch (Exception e) {
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching patient data");
		    }
		}
		
		 @GetMapping("/Chart/ShowPatient")
		    public ModelAndView GotoShowPatient() {
		       ModelAndView mav = new ModelAndView();
		       Map<String, Object> stats = new HashMap<>();
		       stats = cs.GetAllChart(null,null);
		       mav.addObject("stats",stats);
		       mav.setViewName("Chart/ShowPatient");
		       return mav;
		    }
		    
		    @GetMapping("/Chart/DetailPatient")
		    public String GotoDetailPatient() {
		       return "/Chart/DetailPatient";
		    }
		    
		    @PostMapping("/Chart/DetailPatient")
		    public ModelAndView GotoDetailPatient(@RequestParam(name="Gender", required=false) String Gender,
		                                 @RequestParam(name="AgeRange", required=false) String AgeRange){
		       ModelAndView mav = new ModelAndView();
		       Map<String, Object> stats = new HashMap<>();
		       stats = cs.GetAllChart(Gender, AgeRange);
		       mav.addObject("Gender", Gender);
		       mav.addObject("AgeRange", AgeRange);
		       mav.addObject("stats",stats);
		       mav.setViewName("Chart/DetailPatient");
		       return mav;
		    }
		    
		    @GetMapping("/Chart/ShowHospitalRegion")
		    public ModelAndView GotoDetailHospitalRegion() {
		        ModelAndView mav = new ModelAndView();

		        // 기존 지역별 병원 통계
		        Map<String, Object> stats = cs.GetAllHospitalRegion();

		        // 진료과목별 병원 개수 가져오기
		        Map<String, Integer> categoryStatsMap = cs.getHospitalCategoryCount();

		        // 모델에 데이터 추가
		        mav.addObject("stats", stats);
		        mav.addObject("categoryStatsMap", categoryStatsMap);
		        
		        mav.setViewName("Chart/ShowHospitalRegion");
		        return mav;
		    }
		    
		 // Thymeleaf 페이지 반환
		    @GetMapping("/Chart/ShowPatientRegion")
		    public String showPatientRegion(Model model) {
		        return "Chart/ShowPatientRegion"; //  templates/Chart/ShowPatientRegion.html 반환
		    }
}
