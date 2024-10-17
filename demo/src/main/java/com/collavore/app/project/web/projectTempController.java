package com.collavore.app.project.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.project.service.PjTempService;
import com.collavore.app.project.service.ProjectTempVO;
import com.collavore.app.project.service.ProjectVO;

@Controller
public class projectTempController {
	private PjTempService pjtempService;

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("sidemenu", "project_sidebar");
	}

	@Autowired
	public projectTempController(PjTempService pjtempService) {
		this.pjtempService = pjtempService;
	}
	
	// 프로젝트 템플릿 리스트 출력 매핑
	@GetMapping("project/projecttemplist")
	public String projecttempList(Model model) {
		List<ProjectTempVO> list = pjtempService.projecttempList();

		model.addAttribute("projects", list);
		return "project/projectTempList";
	}
	// 프로젝트 템플릿 생성 모달
	@PostMapping("project/projecttempinsert")
	@ResponseBody
	public Map<String, Object> insertAjax(ProjectTempVO projectTempVO) {
		Map<String, Object> map = new HashMap<>();
		
		pjtempService.projecttempinsert(projectTempVO);

		map.put("type", "postAjax");
		map.put("data", projectTempVO);
		return map;
	}
}
