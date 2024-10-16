package com.collavore.app.project.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.project.service.PjService;
import com.collavore.app.project.service.ProjectVO;

@Controller
public class ProjectController {
	private PjService pjService;

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("sidemenu", "project_sidebar");
	}

	@Autowired
	public ProjectController(PjService pjService) {
		this.pjService = pjService;
	}

	// 프로젝트 리스트 출력
	@GetMapping("project/projectlist")
	public String projectList(Model model) {
		List<ProjectVO> list = pjService.projectList();

		model.addAttribute("projects", list);
		return "project/projectList";
	}

	// 프로젝트 생성 모달
	@PostMapping("project/projectinsert")
	@ResponseBody
	public Map<String, Object> insertAjax(ProjectVO projectVO) {
		Map<String, Object> map = new HashMap<>();
		 //System.err.println(projectVO);
		 pjService.projectinsert(projectVO);
		
		map.put("type", "postAjax");
		map.put("data", projectVO);
		return map;
	}
	// 프로젝트 단건 조회 
	@GetMapping("/project/projectinfo/{projNo}")
	@ResponseBody
	public ProjectVO getProjectInfo(@PathVariable int projNo) {
	    return pjService.projectInfo(projNo); 
	}	
	
	// 프로젝트 수정 요청 처리
	@PostMapping("/project/projectupdate")
	@ResponseBody
	public Map<String, Object> updateProject(@RequestBody ProjectVO projectVO) {
	    Map<String, Object> response = new HashMap<>();
	    try {
	        pjService.updateProject(projectVO);
	        response.put("message", "수정 완료");
	        response.put("status", "success");
	    } catch (Exception e) {
	        response.put("message", "수정 실패: " + e.getMessage());
	        response.put("status", "error");
	    }
	    return response;
	}


	// 프로젝트 삭제
	@DeleteMapping("project/projectdelete/{projNo}")
	@ResponseBody
	public String deleteProject(@PathVariable int projNo) {
		pjService.projectDelete(projNo);
		return "삭제 완료";
	}
	
	

	
}
