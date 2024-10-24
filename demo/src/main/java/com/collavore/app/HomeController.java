package com.collavore.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {
	// 사이드 적용
	@ModelAttribute
	public void addAttributes(Model model) {
	    model.addAttribute("sidemenu", "default_sidebar");
	}
	
	@GetMapping("/dashboard")
	public String homePage(Model model) {
		return "home";
	}
}
