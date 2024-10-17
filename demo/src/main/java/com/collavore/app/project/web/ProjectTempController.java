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

import com.collavore.app.project.service.PjTempService;
import com.collavore.app.project.service.ProjectTempVO;
import com.collavore.app.project.service.ProjectWorkTempVO;

@Controller
public class ProjectTempController {
    private PjTempService pjtempService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("sidemenu", "project_sidebar");
    }

    @Autowired
    public ProjectTempController(PjTempService pjtempService) {
        this.pjtempService = pjtempService;
    }
    // 프로젝트 템플릿 리스트
    @GetMapping("project/projecttemplist")
    public String projecttempList(Model model) {
        List<ProjectTempVO> list = pjtempService.projecttempList();
        model.addAttribute("projects", list);
        return "project/projectTempList";
    }   

    @PostMapping("project/projecttempinsert")
    @ResponseBody
    public Map<String, Object> insertAjax(ProjectTempVO projectTempVO) {
        Map<String, Object> response = new HashMap<>();
        
        int generatedId = pjtempService.projecttempinsert(projectTempVO);
        projectTempVO.setProjTempNo(generatedId); 

        response.put("projTempNo", projectTempVO.getProjTempNo());
        response.put("name", projectTempVO.getName());
        response.put("content", projectTempVO.getContent());
        response.put("periodDate", projectTempVO.getPeriodDate());
        
        return response;
    }
    // 템플릿 삭제
    @DeleteMapping("project/projecttempdelete/{projTempNo}")
    @ResponseBody
    public String deleteProject(@PathVariable int projTempNo) {
        pjtempService.projecttempDelete(projTempNo);
        return "삭제 완료";
    }
    
	// 프로젝트 단건 조회 
	@GetMapping("/project/projecttempinfo/{projTempNo}")
	@ResponseBody
	public ProjectTempVO getProjecttempInfo(@PathVariable int projTempNo) {
	    return pjtempService.projecttempInfo(projTempNo); 
	}	
	// 프로젝트 수정 요청 처리
		@PostMapping("/project/projecttempupdate")
		@ResponseBody
		public Map<String, Object> updateProject(@RequestBody ProjectTempVO projectTempVO) {
		    Map<String, Object> response = new HashMap<>();
		    try {
		    	pjtempService.projecttempUpdate(projectTempVO);
		        response.put("message", "수정 완료");
		        response.put("status", "success");
		    } catch (Exception e) {
		        response.put("message", "수정 실패: " + e.getMessage());
		        response.put("status", "error");
		    }
		    return response;
		}
		
	    // 프로젝트 업무 템플릿 리스트
	    @GetMapping("project/projectwrktemplist")
	    public String projectwrktempList(Model model) {
	        List<ProjectWorkTempVO> list = pjtempService.projectWrktempList();
	        List<ProjectTempVO> prolist = pjtempService.projecttempList();
	        model.addAttribute("projects", list);
	        model.addAttribute("prolist", prolist);
	        return "project/projectWorkTempList";
	    } 	
	    // 프로젝트 업무 템플릿 생성
	    @PostMapping("project/projectwrktempinsert")
	    @ResponseBody
	    public Map<String, Object> projwrktempinsertAjax(@RequestBody ProjectWorkTempVO projectworktempVO) {
	        Map<String, Object> response = new HashMap<>();
	        
	        int generatedId = pjtempService.projectwrktempinsert(projectworktempVO);
	        projectworktempVO.setPwtNo(generatedId);
	        
	        
	        int projTempNo = projectworktempVO.getProjTempNo();
	        //System.err.println("pk값: " + generatedId);
	        //System.err.println("템플릿번호: " + projTempNo);
	        
	        response.put("pwtNo", projectworktempVO.getPwtNo());
	        response.put("name", projectworktempVO.getName());
	        response.put("content", projectworktempVO.getContent());
	        response.put("projTempNo", projTempNo);
	        response.put("jobType", projectworktempVO.getJobType());
	        
	        //System.err.println(response);
	        return response;
	    }
	 // 업무템플릿 삭제
		@DeleteMapping("project/projectwrktempdelete/{pwtNo}")
		@ResponseBody
		public String deletewrktempProject(@PathVariable int pwtNo) {
			pjtempService.projectDelete(pwtNo);
			return "삭제 완료";
		}
		
		// 업무템플릿 단건 조회 
		@GetMapping("/project/projectwrktempinfo/{pwtNo}")
		@ResponseBody
		public ProjectTempVO ProjectwrktempInfo(@PathVariable int pwtNo) {
		    return pjtempService.projectwrktempInfo(pwtNo); 
		}	
		// 프로젝트 수정 요청 처리
		@PostMapping("/project/projectwrktempupdate/{pwtNo}")
		@ResponseBody
		public Map<String, Object> updatewrktempProject(@PathVariable int pwtNo, @RequestBody ProjectTempVO projectTempVO) {
		    Map<String, Object> response = new HashMap<>();
		    
		    try {
		        projectTempVO.setProjTempNo(pwtNo); 
		        
		        pjtempService.projectwrktempUpdate(projectTempVO);
		        
		        response.put("message", "수정 완료");
		        response.put("status", "success");
		    } catch (Exception e) {
		        response.put("message", "수정 실패: " + e.getMessage());
		        response.put("status", "error");
		    }
		    
		    return response;
		}

}
