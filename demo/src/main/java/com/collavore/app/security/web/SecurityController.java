package com.collavore.app.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.collavore.app.security.service.UserVO;
import com.collavore.app.security.service.impl.UserDetailsService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class SecurityController {

    private final UserDetailsService userDetailsService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("sidemenu", "default_sidebar");
    }

    @GetMapping("/")
    public String all() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, 
                        @RequestParam String password, 
                        HttpSession session) {
        UserVO user = userDetailsService.findByEmail(email);
    	
        if (user == null || !userDetailsService.authenticate(password, user.getPassword())) {
            return "redirect:/login?error=true";
        }
        //이메일
        session.setAttribute("userEmail", user.getEmail());
        //사원번호
        session.setAttribute("userEmpNo", user.getEmpNo());
        //부서번호
        session.setAttribute("userDeptNo", user.getDeptNo());
        return "redirect:/myPage";
    }

	// 로그아웃 처리
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 세션 무효화
		return "redirect:/login"; // 로그아웃 후 로그인 페이지로 리다이렉트
	}
}
