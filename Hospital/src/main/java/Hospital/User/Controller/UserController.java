package Hospital.User.Controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import Hospital.Board.Service.BoardService;
import Hospital.SpotNews.DTO.SpotNewsResponseDTO;
import Hospital.SpotNews.Service.SpotNewsService;
import Hospital.User.DTO.UserCreateDTO;
import Hospital.User.DTO.UserReadDTO;
import Hospital.User.DTO.UserEditDTO;
import Hospital.User.Service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private BoardService boardService;

	@Autowired
	private SpotNewsService sns;
	
	@GetMapping("/Home") // 홈 화면 출력
	public String GotoHome(HttpSession session, Model model) {
		String UserId = (String)session.getAttribute("UserId");
		if(UserId != null) {
			UserReadDTO uiDTO = this.userService.UserInfoRead(UserId);
			model.addAttribute("UserId",UserId);
			model.addAttribute("UserName",uiDTO.getUserName());
			session.setAttribute("UserId",UserId);
			session.setAttribute("UserName",uiDTO.getUserName());
			
		}
		else {
			model.addAttribute("UserId",null);
			model.addAttribute("UserName",null);
		}
		   //  의료 속보 데이터 추가
        List<SpotNewsResponseDTO> spotNewsList = sns.getAllNews();
        model.addAttribute("spotNewsList", spotNewsList);
        model.addAttribute("recentPosts", boardService.RecentBoardList());

        return "/Home"; //Thymeleaf에서 Home.html을 찾음
	}
	
	@GetMapping("/UserPage/Login") // 로그인 화면으로 이동
	public String GotoLogin() {
		return "/UserPage/Login";
	}
	
	@PostMapping("/UserPage/Login") // 로그인 버튼 누르면 실행 함수
	public String Login(@RequestParam("UserId") String UserId,
						@RequestParam("UserPw") String UserPw,
						HttpSession session, Model model) throws Exception  {
		boolean Result = this.userService.UserLogin(UserId,UserPw);
		
		if(Result == true){
			session.setAttribute("UserId", UserId);
			System.out.println(UserId);
			return "redirect:/Home";
		}
		else {
			model.addAttribute("Error", "아이디 또는 비밀번호가 틀렸습니다.");
			return "/UserPage/Login";
		}
	}
	
	
	
	@GetMapping("/UserPage/SignUp") // 회원가입 버튼을 누르면 회원가입 화면으로 이동 
	public String GotoSignUp() {
		return "/UserPage/SignUp";
	}
	
	@PostMapping("/UserPage/SignUp") // 회원가입 화면에서, 입력 후 회원가입 완료 누르면 실행 후 이동
	public String SignUp(UserCreateDTO ucDTO, RedirectAttributes rda) throws Exception {
		if(this.userService.UserIdCheck(ucDTO.getUserId())) {
			rda.addFlashAttribute("Error","사용할 수 없는 아이디입니다.");
			rda.addFlashAttribute("UserId",ucDTO.getUserId());
			rda.addFlashAttribute("UserName",ucDTO.getUserName());
			rda.addFlashAttribute("UserRegNum",ucDTO.getUserRegNum());
			rda.addFlashAttribute("UserPhone",ucDTO.getUserPhone());
			rda.addFlashAttribute("UserGender",ucDTO.getUserGender());
			rda.addFlashAttribute("UserAddress1",ucDTO.getUserAddress1());
			rda.addFlashAttribute("UserAddress2",ucDTO.getUserAddress2());
			return "redirect:/UserPage/SignUp";
		}
		this.userService.UserRegister(ucDTO);
		return "/UserPage/SignUpComplete";
	}
	
	
	@PostMapping("/UserPage/CheckUserId") 
	@ResponseBody// 아이디 중복체크
	public Map<String, String> CheckUserId(@RequestBody Map<String, String> requestData) {
	    String userId = requestData.get("userId");
	    Map<String, String> response = new HashMap<>();
	    if (this.userService.UserIdCheck(userId)) {
	        response.put("status", "error");
	        response.put("message", "아이디가 이미 존재합니다.");
	    } else {
	        response.put("status", "success");
	        response.put("message", "사용 가능한 아이디입니다.");
	    }	    
	    return response;
	}
	
	
	@GetMapping("/UserPage/Logout") // 로그아웃
	public String Logout(HttpSession session) {
		session.invalidate();
		return "redirect:/Home";
	}
	
	@GetMapping("/UserPage/EditUserInfo") // 회원정보 수정 화면으로 이동
	public ModelAndView GotoEditUserInfo(HttpSession session, Model model) {
		ModelAndView mav = new ModelAndView();
		String UserId = (String)session.getAttribute("UserId");
		
		if(UserId == null) {
			mav.setViewName("redirect:/Home");
			return mav;
		}
		
		model.addAttribute("UserId",UserId);
		UserReadDTO uiDTO = this.userService.UserInfoRead(UserId);
		mav.addObject("UserInfo",uiDTO);
		mav.setViewName("/UserPage/EditUserInfo");
		return mav;
	}
	
	@PostMapping("/UserPage/EditUserInfo")
	public String EditUserInfo(UserEditDTO uieDTO, RedirectAttributes rda) throws Exception {
	    try {
	        this.userService.UserModify(uieDTO);
	        return "/UserPage/EditUserInfoResult"; // 성공 시 결과 페이지로 이동
	    } catch (IllegalArgumentException e) {
	        //  에러 메시지를 RedirectAttributes에 담기
	        rda.addFlashAttribute("error", e.getMessage());
	        rda.addFlashAttribute("UserInfo", uieDTO); // 입력값 유지
	        return "redirect:/UserPage/EditUserInfo";  // 다시 수정 페이지로 이동
	    }
	}
	
	@GetMapping("/UserPage/FindUserId")
	public String GotoFindUserId() {
		return "/UserPage/FindUserId";
	}
	
	@PostMapping("/UserPage/FindUserId")
	public ModelAndView FindUserId(@RequestParam("UserName") String UserName,
								   @RequestParam("UserRegNum") String UserRegNum,
								   Model model) {
		ModelAndView mav = new ModelAndView();
		UserReadDTO uiDTO = new UserReadDTO();
		uiDTO = this.userService.FindUserId(UserName, UserRegNum);
		if(uiDTO != null) {
			mav.addObject("FindUserInfoResult", uiDTO);
			mav.setViewName("/UserPage/FindUserIdResult");
			return mav;
		}
		else {
			model.addAttribute("error","입력하신 정보와 일치하는 회원정보가 없습니다.");
			mav.setViewName("/UserPage/FindUserId");
			return mav;
		}
	}
	
	@GetMapping("/UserPage/FindUserIdResult")
	public String GotoFindUserIdResult() {
		return "/UserPage/FindUserIdResult";
	}
	
	@GetMapping("/UserPage/FindUserPw")
	public String GotoFindUserPw() {
		return "/UserPage/FindUserPw";
	}
	
	@PostMapping("/UserPage/FindUserPw")
	public ModelAndView FindUserPw(@RequestParam("UserId") String UserId,
								   @RequestParam("UserName") String UserName,
								   @RequestParam("UserRegNum") String UserRegNum,
								   Model model) {
		ModelAndView mav = new ModelAndView();
		UserReadDTO uiDTO = new UserReadDTO();
		uiDTO = this.userService.FindUserPw(UserId, UserName, UserRegNum);
		if(uiDTO != null) {
			mav.addObject("FindUserInfoResult",uiDTO);
			mav.setViewName("/UserPage/ResetUserPw");
			return mav;
		}
		else {
			model.addAttribute("error","입력하신 정보와 일치하는 회원정보가 없습니다.");
			mav.setViewName("/UserPage/FindUserPw");
			return mav;
		}
	}

	@PostMapping("/UserPage/ResetUserPw")
	public String ResetUserPw(@RequestParam("UserId") String UserId,
							  @RequestParam("UserPw") String UserPw) throws Exception {
		
		this.userService.ResetUserPw(UserId, UserPw);
		return "/UserPage/ResetUserPwResult";
	}
	
	@GetMapping("/UserPage/DeleteUserId") // 회원 탈퇴 페이지 이동
	   public String GotoDeleteUserId(HttpSession session, Model model) {
	      String UserId = (String) session.getAttribute("UserId");
	      model.addAttribute("UserId",UserId);
	      
	      if(UserId.equals("admin")) {
	         return "/Home";
	      }
	      else {
	         return "/UserPage/DeleteUserId";
	      }
	   }
	
	
	@PostMapping("/UserPage/DeleteUserId") // 회원 탈퇴 진행
	public String DeleteUserId(@RequestParam("UserId") String UserId,
							   @RequestParam("UserPw") String UserPw,
							   HttpSession session, Model model) throws Exception {
		boolean result = this.userService.DeleteUserId(UserId, UserPw);
		if(result == true) {
			session.invalidate();
			return "/UserPage/DeleteUserIdResult";
		}
		else {
			model.addAttribute("UserId",UserId);
			model.addAttribute("Error","비밀번호가 일치하지 않습니다.");
			return "/UserPage/DeleteUserId";
		}
	}
	
	@GetMapping("/UserPage/DeleteUserIdResult") // 회원 탈퇴 결과
	public String GotoDeleteUserIdResult() {
		return "/UserPage/DeleteUserIdResult";
	}
	
}
