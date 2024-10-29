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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.project.service.PjService;
import com.collavore.app.project.service.PjTempService;
import com.collavore.app.project.service.ProjectDWorkTempVO;
import com.collavore.app.project.service.ProjectTempVO;
import com.collavore.app.project.service.ProjectVO;
import com.collavore.app.project.service.ProjectWorkTempVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectTempController {
	private final PjService pjService;
    private final PjTempService pjtempService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("sidemenu", "project_sidebar");
    }

   
    // 프로젝트 템플릿 리스트
    @GetMapping("/projecttemplist")
    public String projecttempList(Model model) {
        List<ProjectTempVO> list = pjtempService.projecttempList();

        model.addAttribute("projects", list);
        return "project/projectTempList";
    }   

    @PostMapping("/projecttempinsert")
    @ResponseBody
    public Map<String, Object> insertAjax(ProjectTempVO projectTempVO) {
        Map<String, Object> response = new HashMap<>();
        
        pjtempService.projecttempinsert(projectTempVO);
     
        response.put("projTempNo", projectTempVO.getProjTempNo());
        response.put("name", projectTempVO.getName());
        response.put("content", projectTempVO.getContent());
        response.put("periodDate", projectTempVO.getPeriodDate());
        
        return response;
    }
    // 템플릿 삭제
    @DeleteMapping("/projecttempdelete/{projTempNo}")
    @ResponseBody
    public String deleteProject(@PathVariable int projTempNo) {
        pjtempService.projecttempDelete(projTempNo);
        return "삭제 완료";
    }
    
	// 프로젝트 단건 조회 
	@GetMapping("/projecttempinfo/{projTempNo}")
	@ResponseBody
	public ProjectTempVO getProjecttempInfo(@PathVariable int projTempNo) {
	    return pjtempService.projecttempInfo(projTempNo); 
	}	
	// 프로젝트 수정 요청 처리
		@PostMapping("/projecttempupdate")
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
	    @GetMapping("/projectwrktemplist")
	    public String projectwrktempList(Model model) {
	        List<ProjectWorkTempVO> list = pjtempService.projectWrktempList();
	        List<ProjectTempVO> prolist = pjtempService.projecttempList();
	        List<ProjectVO> jobs = pjService.jobsList(); 
	        
	        model.addAttribute("jobs", jobs);
	        model.addAttribute("projects", list);
	        model.addAttribute("prolist", prolist);
	        return "project/projectWorkTempList";
	    } 	
	    // 프로젝트 업무 템플릿 생성
	    @PostMapping("/projectwrktempinsert")
	    @ResponseBody
	    public Map<String, Object> projwrktempinsertAjax(@RequestBody ProjectWorkTempVO projectworktempVO) {
	        Map<String, Object> response = new HashMap<>();
	        
	        int generatedId = pjtempService.projectwrktempinsert(projectworktempVO);
	
	        //System.err.println("pk값: " + generatedId);
	        //System.err.println("템플릿번호: " + projTempNo);
	        
	        response.put("pwtNo", projectworktempVO.getPwtNo());
	        response.put("name", projectworktempVO.getName());
	        response.put("content", projectworktempVO.getContent());
	        response.put("projTempNo", generatedId);
	        response.put("jobType", projectworktempVO.getJobNo());
	        
	        //System.err.println(response);
	        return response;
	    }
	 // 업무템플릿 삭제
		@DeleteMapping("/projectwrktempdelete/{pwtNo}")
		@ResponseBody
		public String deletewrktempProject(@PathVariable int pwtNo) {
			pjtempService.projectDelete(pwtNo);
			return "삭제 완료";
		}
		
		// 업무템플릿 단건 조회 
		@GetMapping("/projectwrktempinfo/{pwtNo}")
		@ResponseBody
		public ProjectWorkTempVO ProjectwrktempInfo(@PathVariable int pwtNo) {
		    return pjtempService.projectwrktempInfo(pwtNo); 
		}	
		
		// 프로젝트 수정 요청 처리
		@PostMapping("/projectwrktempupdate/{pwtNo}")
		@ResponseBody
		public Map<String, Object> updatewrktempProject(@PathVariable int pwtNo, @RequestBody ProjectWorkTempVO projectworkTempVO) {
		    Map<String, Object> response = new HashMap<>();
		    try {
		    	projectworkTempVO.setPwtNo(pwtNo); 
		        pjtempService.projectwrktempUpdate(projectworkTempVO);

		        response.put("message", "수정 완료");
		        response.put("status", "success");
		    } catch (Exception e) {
		        response.put("message", "수정 실패: " + e.getMessage());
		        response.put("status", "error");
		    }
		    return response;
		}

		
	    // 프로젝트 템플릿 리스트
	    @GetMapping("/projectDwrktemplist")
	    public String projectDwrktempList(Model model) {
	        List<ProjectDWorkTempVO> list = pjtempService.projectDwrktemplist();
	        List<ProjectWorkTempVO> worklist = pjtempService.projectWrktempList();
	        
	        model.addAttribute("projects", list);
	        model.addAttribute("wrkproj", worklist);
	        return "project/projectDeteilWorkTempList";
	    }   
	    
	 // 프로젝트 상세업무 템플릿 생성
	    @PostMapping("/projectDwrktempinsert")
	    @ResponseBody
	    public Map<String, Object> projDwrktempinsertAjax(@RequestBody ProjectDWorkTempVO projectDworktempVO) {
	        Map<String, Object> response = new HashMap<>();

	        int generatedId =  pjtempService.projectDwrktempinsert(projectDworktempVO);
	        projectDworktempVO.setPdwtNo(generatedId);
	        
	        int getPwtNo = projectDworktempVO.getPwtNo();
	        // 응답 데이터 설정
	        System.err.println(projectDworktempVO);
	        System.err.println(getPwtNo);
	        
	        response.put("pdwtNo", projectDworktempVO.getPdwtNo());
	        response.put("name", projectDworktempVO.getName());
	        response.put("content", projectDworktempVO.getContent());
	        response.put("pwtNo", getPwtNo); 
	        response.put("importance", projectDworktempVO.getImportance()); 

	        return response;
	    }
		 // 상세 업무템플릿 삭제
			@DeleteMapping("/projectdwrktempdelete/{pdwtNo}")
			@ResponseBody
			public String deletedwrktempProject(@PathVariable int pdwtNo) {
				pjtempService.projectdwrktempDelete(pdwtNo);
				return "삭제 완료";
			}
			
			// 상세 업무템플릿 단건 조회 
			@GetMapping("/projectDwrktempinfo/{pdwtNo}")
			@ResponseBody
			public ProjectDWorkTempVO ProjectDwrktempInfo(@PathVariable int pdwtNo) {
			    return pjtempService.projectDwrktempInfo(pdwtNo); 
			}
			
			// 상세업무템플릿 수정 요청 처리
			@PostMapping("/projectDwrktempupdate")
			@ResponseBody
			public Map<String, Object> updateDwrktempProject(@RequestBody ProjectDWorkTempVO projectDworktempVO) {
			    Map<String, Object> response = new HashMap<>();
			    System.err.println(projectDworktempVO);
			    try {
			    	pjtempService.projectdwrktempUpdate(projectDworktempVO);
			        response.put("message", "수정 완료");
			        response.put("status", "success");
			    } catch (Exception e) {
			        response.put("message", "수정 실패: " + e.getMessage());
			        response.put("status", "error");
			    }
			    return response;
			}			

}
