package com.collavore.app.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String logins(@RequestParam(value = "error", required = false) String error, HttpSession session,
			Model model) {
		if (error != null) {
			model.addAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
		}
		return "login";
	}
}
