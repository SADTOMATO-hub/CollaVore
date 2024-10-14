package com.collavore.app.cals.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
@Controller
public class CalsController {
	@ModelAttribute
	public void addAttributes(Model model) {
	    model.addAttribute("sidemenu", "calendar_sidebar");
	}
	
	@GetMapping("/cal/calList")
	public String cal() {
		return "calendar/schList";
	}// end homepage
	
}
