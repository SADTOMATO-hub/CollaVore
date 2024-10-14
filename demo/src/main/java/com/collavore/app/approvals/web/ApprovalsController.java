package com.collavore.app.approvals.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ApprovalsController {
	@ModelAttribute
	public void addAttributes(Model model) {
	    model.addAttribute("sidemenu", "approvals_sidebar");
	}
	//템플릿 생성 페이지
	@GetMapping("approvals/createTemplateForm")
	 public String createTemplatePage() {
		return "approvals/createTemplateFrom";
	}
	
}
