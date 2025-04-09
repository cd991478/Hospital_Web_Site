package Hospital.Global;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute("UserId")
    public String getUserId(HttpSession session) {
        return (String) session.getAttribute("UserId");  // 세션에서 UserId 가져오기
    }
    @ModelAttribute("UserName")
    public String getUserName(HttpSession session) {
        return (String) session.getAttribute("UserName");  // 세션에서 UserName 가져오기
    }
}
