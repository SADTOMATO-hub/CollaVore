package com.collavore.app.project.web;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.collavore.app.project.service.PjService;
import com.collavore.app.project.service.ProjectVO;

import lombok.RequiredArgsConstructor;

@Controller
public class ProjectController {
	private PjService pjService;
	
	@ModelAttribute
	public void addAttributes(Model model) {
	    model.addAttribute("sidemenu", "project_sidebar");
	}
	
	@Autowired
	public ProjectController(PjService pjService){
		this.pjService = pjService;
	}
	
	@GetMapping("projectlist")
	public String boardList(Model model) {
		List<ProjectVO> list = pjService.projectList();
		
		model.addAttribute("projects", list);
		return "project/projectList"; 
	}
}
