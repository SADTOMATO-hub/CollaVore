package com.collavore.app.project.web;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	
	@GetMapping("project/projectlist")
	public String projectList(Model model) {
		List<ProjectVO> list = pjService.projectList();
		
		model.addAttribute("projects", list);
		return "project/projectList"; 
	}
	
	@PostMapping("project/projectinsert")
	@ResponseBody
	public Map<String, Object> insertAjax(ProjectVO projectVO){ 
		Map<String, Object> map = new HashMap<>();
		//System.err.println(projectVO); 
		pjService.projectinsert(projectVO);
		
		map.put("type", "postAjax");
		map.put("data", projectVO);
		return map;
	}
//	@DeleteMapping("project/projectdelete/{projNo}")
//	@ResponseBody
//	public String deleteProject(int projNo) {
//	        pjService.projectDelete(projNo);
//	        return "삭제 완료"; 
//	}
}
