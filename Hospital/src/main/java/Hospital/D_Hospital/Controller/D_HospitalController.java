package Hospital.D_Hospital.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import Hospital.D_Hospital.DTO.D_HospitalListDTO;
import Hospital.D_Hospital.Entity.D_Hospital;
import Hospital.D_Hospital.Entity.D_HospitalRepository;
import Hospital.D_Hospital.Service.D_HospitalService;

@Controller
public class D_HospitalController {
	
	@Autowired
	private D_HospitalService d_hs;
	@Autowired
	private D_HospitalRepository d_hr;
	@GetMapping("/AllHospitalList") // 전체 병원 조회
	public ModelAndView GotoAllHospitalList(@RequestParam(name="page", required=false) Integer page,
			 								@RequestParam(value = "H_Region", required = false) String H_Region,
			 								@RequestParam(value = "H_Categorie", required = false) String H_Categorie,
			 								Model model) {
		final int PageSize = 10;
		
        System.out.println("======== 컨트롤러 진입 ========");
        System.out.println("H_Categorie: " + H_Categorie);
        System.out.println("H_Region: " + H_Region);
        if(H_Categorie == null || H_Region == null) {
        	ModelAndView mav = new ModelAndView();
            mav.addObject("D_HospitalList", null);
            mav.setViewName("AllHospitalList");
            return mav;
        }
       
        if (page == null || page < 0) {
	        page = 0;  // 첫 페이지를 기본값으로 설정
	    }
        //
        List<D_HospitalListDTO> HospitalList = this.d_hs.D_HospitalListSearchPage(page, PageSize,H_Categorie, H_Region);
        
        List<D_Hospital> allhospital = this.d_hr.findAll();
        long totalElements = allhospital.stream()
                .filter(hospital -> hospital.getH_Region().equals(H_Region)&&hospital.getH_Categorie().contains(H_Categorie))
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
	    mav.addObject("H_Region",(String)H_Region);
	    mav.addObject("H_Categorie", (String)H_Categorie);
	    mav.setViewName("AllHospitalList");
	    return mav;
	}
	
}
