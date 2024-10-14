package com.collavore.app.project.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class projectController {
	@GetMapping("projectlist")
	public String boardList(Model model) {

		return "project/projectList"; 
	}
}
