package com.collavore.app.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
	public String logins(HttpSession session, Model model) {
	    String loginError = (String) session.getAttribute("loginError");
	    if (loginError != null) {
	        model.addAttribute("loginError", loginError);
	        session.removeAttribute("loginError"); // 세션에서 제거하여 리프레시 시 메시지가 유지되지 않도록 함
	    }
	    return "login";
	}

}
