package Hospital.Global;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@GetMapping("/AdminPage/Menu")
	   public ModelAndView GotoAdminMenu(HttpSession session, Model model) {
	      ModelAndView mav = new ModelAndView();
	      String UserId = (String)session.getAttribute("UserId");
	      if(UserId.equals("admin")) {
	         mav.setViewName("AdminPage/Menu");
	         return mav;
	      }
	      else {
	         mav.setViewName("Home");
	         return mav;
	      }  
	   }
}
