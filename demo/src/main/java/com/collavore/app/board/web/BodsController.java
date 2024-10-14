package com.collavore.app.board.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class BodsController {
	@ModelAttribute
	public void addAttributes(Model model) {
	    model.addAttribute("sidemenu", "board_sidebar");
	}

	@GetMapping("/board")
	public String homepage() {
		return "board/bodsList";
	}// end homepage
}
