 package com.collavore.app.project.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.collavore.app.common.service.FileUtill;
import com.collavore.app.project.service.PjService;
import com.collavore.app.project.service.PjTempService;
import com.collavore.app.project.service.ProjectDWorkTempVO;
import com.collavore.app.project.service.ProjectFilesVO;
import com.collavore.app.project.service.ProjectFoldersVO;
import com.collavore.app.project.service.ProjectTempVO;
import com.collavore.app.project.service.ProjectVO;
import com.collavore.app.service.HomeService;
import com.collavore.app.service.HomeVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
    
@Controller
@RequiredArgsConstructor
public class ProjectController {
	private final PjService pjService;
	private final PjTempService pjtempService;
	private final HomeService homeService;
	@Value("${file.upload.path}") // 메모리에 올라가 있는 변수값을 가져오기 때문에 표현이 다름아아아아아
	private String uploadPath;

	@ModelAttribute
	public void addAttributes(Model model, HttpSession session) {
		List<HomeVO> employeesInfo = homeService.empList();
		model.addAttribute("employees", employeesInfo);
		
		String userAdmin = (String) session.getAttribute("userAdmin");
		model.addAttribute("userAdmin", userAdmin);

		@SuppressWarnings("unchecked")
		List<String> menuAuth = (List<String>) session.getAttribute("menuAuth");
		model.addAttribute("menuAuth", menuAuth);
		
		model.addAttribute("sidemenu", "project_sidebar");
	}

	// 프로젝트 리스트 출력
	@GetMapping("project/projectlist")
	public String projectList(Model model, HttpSession session) {
		Integer empNo = (Integer) session.getAttribute("userEmpNo");
		List<ProjectVO> list = pjService.projectList();
		List<ProjectTempVO> templist = pjtempService.projecttempList();
		List<ProjectVO> emplist = pjService.empList();
		ProjectVO gitInfo = pjService.compGitInfo();
		
		model.addAttribute("empNo", empNo);
		model.addAttribute("projects", list);
		model.addAttribute("templist", templist);
		model.addAttribute("emp", emplist);
		model.addAttribute("gitInfo", gitInfo);
		return "project/projectList";
	}

	// 프로젝트 생성 모달
	@PostMapping("project/projectinsert")
	@ResponseBody
	public Map<String, Object> insertAjax(ProjectVO projectVO) {
		Map<String, Object> map = new HashMap<>();
		
		
		//프로젝트생성
		System.err.println(projectVO.getIsTemplate());
		pjService.projectinsert(projectVO);
		String prjname = projectVO.getName();
		pjService.projectfolderinsert(projectVO);
		if("i2".equals(projectVO.getIsTemplate())) {
		//템플릿 업무 리스트 출력
		List<ProjectTempVO> projwrklist = pjtempService.projectwrktemplistInfo(projectVO.getProjTempNo());
		for (ProjectTempVO user : projwrklist) {
			projectVO.setName(user.getName());
			projectVO.setContent(user.getContent());
			projectVO.setProjTempNo(user.getProjTempNo());
			projectVO.setJobNo(user.getJobNo());
			// System.err.println(projectVO);
			// 업무 생성 
			pjService.projectwrkinsert(projectVO);
			// 상세업무 리스트 출력
			List<ProjectDWorkTempVO> projdwrklist = pjtempService.projectDwrktemplistInfo(user.getPwtNo());
			for (ProjectDWorkTempVO dwrk : projdwrklist) {
				projectVO.setName(dwrk.getName());
				projectVO.setContent(dwrk.getContent());
				projectVO.setPwtNo(dwrk.getPwtNo());
				projectVO.setImportance(dwrk.getImportance());
				// System.err.println("-----------------------------------");
				// System.err.println(projectVO);
				// 상세업무 생성
				pjService.projectdwrkinsert(projectVO);
				}
			}
		}
		//System.err.println(projectVO);
		
		ProjectVO job = pjService.projectInfo(projectVO.getProjNo());
		// pjService.projectwrkinsert(projectVO);

		// pjService.projectdwrkinsert(projectVO);
		System.err.println(job.getJobName());	
		map.put("jobName", job.getJobName());
		map.put("name", prjname);
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
			//
			pjService.updateProject(projectVO);
			//System.err.println(projectVO);
			
			ProjectVO job = pjService.projectInfo(projectVO.getProjNo());
			System.err.println(projectVO.getStatus());
			
			response.put("status2", projectVO.getStatus());
			response.put("jobName", job.getJobName());
			response.put("message", "수정 완료");
			response.put("status", "success");
		} catch (Exception e) {
			e.printStackTrace(); // 예외의 상세 정보를 출력
			response.put("message", "수정 실패: " + e.getMessage());
			response.put("status", "error");
		}
		return response;
	}

	// 프로젝트 삭제
	@DeleteMapping("project/projectdelete/{projNo}")
	@ResponseBody
	public String deleteProject(@PathVariable int projNo) {
	
		pjService.projectComtDel(projNo);
		List<ProjectVO> projectVOList = pjService.projectwrkList(projNo);
		pjService.projectwrkDelete(projNo);
		for(ProjectVO info :  projectVOList) {
			pjService.projectdwrkDelete(info.getPwNo());
		}
		ProjectVO projfolderinfo = pjService.projectfolderInfo(projNo);
		List<ProjectVO> projectfileList = pjService.projfileinfo(projfolderinfo.getPfNo());
		
		for(ProjectVO fileinfo: projectfileList) {
			pjService.projfiledel(fileinfo.getPfNo());
		}
		pjService.projectfolderDelete(projNo);
		
		pjService.projectDelete(projNo);
		return "삭제 완료";
	}
	
	// 프로젝트 업무 삭제
		@DeleteMapping("project/projectwrkdel/{pwNo}")
		@ResponseBody
		public String deletewrkProject(@PathVariable int pwNo) {
			// 업무의 하위 상세업무 삭제
			pjService.projectdwrkDelete(pwNo);
			
			// 업무 삭제 
			pjService.projectwrkoneDelete(pwNo);
			return "삭제 완료";
		}

		// 프로젝트 상세 업무 삭제
		@DeleteMapping("project/projectdwrkdel/{pdwNo}")
		@ResponseBody
		public String deletedwrkProject(@PathVariable int pdwNo) {
			pjService.projectdwrkDelete(pdwNo);
			return "삭제 완료";
		}
		
	// 프로젝트 폴더 관리
	@GetMapping("project/projectfilelist")
	public String projectFileList(Model model) {
		List<ProjectFoldersVO> list = pjService.projectfolderList();

		model.addAttribute("projects", list);
		return "project/projectFilesList"; // view 이름 수정 필요
	}

	// 프로젝트 파일 관리
	@GetMapping("project/projectfileslist/{pfNo}")
	@ResponseBody
	public List<ProjectFilesVO> projectFilesList(@PathVariable int pfNo) {
		return pjService.projectfileList(pfNo);
	}
	
	// 프로젝트 파일 삭제
		@DeleteMapping("project/fileDelete/{projFileNo}")
		@ResponseBody
		public String  file(@PathVariable int projFileNo) {
			pjService.fileDelete(projFileNo);
			return "삭제 완료";
		}

	// 파일 업로드 처리 메소드
	@PostMapping("/project/uploadfile")
	public String uploadFile(@RequestPart("file") MultipartFile file, @ModelAttribute ProjectFilesVO ProjectFilesVO,
			Model model) {
		if (file.isEmpty()) {
			model.addAttribute("message", "파일이 비어 있습니다.");
			return "redirect:/project/projectfilelist"; // 파일이 비어 있으면 목록으로 리다이렉트
		}

		try {
			// 파일 저장 경로 설정
			String uploadDir = uploadPath; // 실제 파일이 저장될 경로 설정
			Path path = Paths.get(uploadDir + file.getOriginalFilename());
			Files.createDirectories(path.getParent()); // 필요한 디렉토리 생성
			Files.write(path, file.getBytes()); // 파일 저장

			// ProjectFilesVO에 파일 정보 설정
			ProjectFilesVO.setFilePath(uploadDir);
			ProjectFilesVO.setFileSize(file.getSize());
			ProjectFilesVO.setName(file.getOriginalFilename());
			ProjectFilesVO.setExtension(FileUtill.getFileExtension(file.getOriginalFilename()));

			pjService.saveFile(file.getOriginalFilename(), ProjectFilesVO);

			model.addAttribute("message", "파일 업로드 성공!");
		} catch (Exception e) {
			model.addAttribute("message", "파일 업로드 실패: " + e.getMessage());
		}

		return "redirect:/project/projectfilelist"; // 처리 후 목록으로 리다이렉트
	}

	// 파일 다운로드
	@GetMapping("/project/downloadfile/{projFileNo}")
	public ResponseEntity<FileSystemResource> downloadFile(@PathVariable Long projFileNo) {
		// 파일 정보를 가져오는 서비스 메소드
		ProjectFilesVO fileDetails = pjService.getFileDetails(projFileNo);
		if (fileDetails == null) {
			return ResponseEntity.notFound().build();
		}

		Path filePath = Paths.get(fileDetails.getFilePath(), fileDetails.getName()); // 파일 경로
		FileSystemResource resource = new FileSystemResource(filePath.toFile());

		// 파일이 존재하는지 체크
		if (!resource.exists() || !resource.isFile()) {
			return ResponseEntity.notFound().build();
		}

		try {
			// 파일명 URL 인코딩
			String encodedFileName = URLEncoder.encode(fileDetails.getName(), StandardCharsets.UTF_8.toString());
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
					.header(HttpHeaders.CONTENT_TYPE, "application/octet-stream") // 콘텐츠 타입 설정
					.body(resource);
		} catch (UnsupportedEncodingException e) {
			// 인코딩 오류 처리
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 프로젝트 리스트 출력
	@GetMapping("project/projectworklist")
	public String projectworkList(Model model) {
		List<ProjectVO> list = pjService.projecttreeList();

		List<ProjectVO> departments = pjService.departmentsList();
		List<ProjectVO> jobs = pjService.jobsList();

		model.addAttribute("projects", list);
		model.addAttribute("jobs", jobs);
		model.addAttribute("dept", departments);
		return "project/projectWorkList";
	}

	// 프로젝트 업무 모달
	@PostMapping("project/projectwrkinsert")
	@ResponseBody
	public Map<String, Object> insertwrkAjax(ProjectVO projectVO) {
		Map<String, Object> map = new HashMap<>();
		// System.err.println(projectVO);
		pjService.projectwrkinsert(projectVO);

		map.put("type", "postAjax");
		map.put("data", projectVO);
		return map;
	}

	// 프로젝트 상세업무 모달
	@PostMapping("project/projectdwrkinsert")
	@ResponseBody
	public Map<String, Object> inserdtwrkAjax(ProjectVO projectVO) {
		Map<String, Object> map = new HashMap<>();
		String selNo = projectVO.getSelPwNo();
		String selParentNo = projectVO.getSelParentPdwNo();
		 //System.err.println(projectVO);
		switch (selParentNo.substring(0, 1)) {
		case "W":
			projectVO.setPwNo(Integer.parseInt(selParentNo.replace("W", "")));
			projectVO.setParentPdwNo(null);
			break;
		default:
			int searchPwNo = pjService.selectPwNo(Integer.parseInt(selParentNo));
			projectVO.setPwNo(searchPwNo);
			projectVO.setParentPdwNo(Integer.parseInt(selParentNo));
		}

		pjService.projectdwrkinsert(projectVO);

		map.put("type", "postAjax");
		map.put("data", projectVO);
		return map;
	}

	/*
	 * // 업무 단건 조회
	 * 
	 * @GetMapping("/project/projectwrkinfo/{pdwNo}")
	 * 
	 * @ResponseBody public int getProjectwrkInfo(@PathVariable int pdwNo) { return
	 * pjService.selectPwNo(pdwNo); }
	 */

	// 업무 단건조회리스트
	@GetMapping("/project/projectwrklistinfo/{pwNo}")
	@ResponseBody
	public ProjectVO getProjectwrklistInfo(@PathVariable int pwNo) {
		return pjService.projectwrkInfo(pwNo);
	}

	// 상세업무 단건조회리스트
	@GetMapping("/project/projectdwrkinfo/{pdwNo}")
	@ResponseBody
	public ProjectVO getProjectdwrkInfo(@PathVariable int pdwNo) {
		return pjService.projectdwrkInfo(pdwNo);
	}

	// 프로젝트 단건 조회
	@GetMapping("/project/projectlistinfo/{pwNo}")
	@ResponseBody
	public ProjectVO getProjectInfo2(@PathVariable int pwNo) {
		return pjService.projectInfo(pwNo);
	}

	// 프로젝트 업무 수정 요청 처리
	@PostMapping("/project/projectwrkupdate")
	@ResponseBody
	public Map<String, Object> updatewrkProject(@RequestBody ProjectVO projectVO) {
		Map<String, Object> response = new HashMap<>();
		try {
			pjService.updatewrkProject(projectVO);
			response.put("message", "수정 완료");
			response.put("status", "success");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "수정 실패: " + e.getMessage());
			response.put("status", "error");
		}
		return response;
	}

	// 프로젝트 상세업무 수정 요청 처리
	@PostMapping("/project/projectdwrkupdate")
	@ResponseBody
	public Map<String, Object> updatedwrkProject(@RequestBody ProjectVO projectVO) {
		Map<String, Object> response = new HashMap<>();
		try {
			//System.err.println(projectVO);
			pjService.updatedwrkProject(projectVO);
			response.put("message", "수정 완료");
			response.put("status", "success");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "수정 실패: " + e.getMessage());
			response.put("status", "error");
		}
		return response;
	}

//	// 상세업무 코멘트 단건 리스트
//	@GetMapping("/project/projecdwrkcomtstinfo/{pdwNo}")
//	@ResponseBody
//	public List<ProjectVO> projectDWrkComtInfo(@PathVariable int pdwNo) {
//		return pjService.projectDWrkComtInfo(pdwNo);
//	}
//	
	// 상세업무 코멘트 단건 리스트
	@GetMapping("/project/projecdwrkcomtstinfo/{pdwNo}")
	@ResponseBody
	public Map<String, Object> projectDWrkComtInfo(@PathVariable int pdwNo) {
	    // 프로젝트 업무 코멘트 정보를 가져오는 메서드 호출
	    List<ProjectVO> projectComments = pjService.projectDWrkComtInfo(pdwNo);
	    
	    ProjectVO projectManager = pjService.projectdwrkInfo(pdwNo);
	    List<ProjectVO> selectmgrs = Collections.singletonList(projectManager);
	    System.err.println(projectComments);
	    
	    // 결과를 하나의 Map으로 결합하여 반환
	    Map<String, Object> response = new HashMap<>();
	    response.put("comments", projectComments);
	    response.put("mgrs", selectmgrs);

	    return response; // 결합된 정보를 반환
	}
	
	// 상세업무 코멘트 생성
	@PostMapping("project/projectdwrkcomtinsert")
	@ResponseBody
	public Map<String, Object> dwrkcomtinsertAjax(@RequestBody ProjectVO projectVO) {
		Map<String, Object> map = new HashMap<>();
		//System.err.println(projectVO);
		pjService.projectdwrkcomtinsert(projectVO);

		// regDate를 포맷팅하여 응답에 추가
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedRegDate = projectVO.getRegDate() != null ? sdf.format(projectVO.getRegDate()) : null;

		map.put("type", "postAjax");
		map.put("data", projectVO);
		map.put("content", projectVO.getContent()); // message
		map.put("regDate", formattedRegDate); // 현재 시간 등
		return map;
	}

	// 프로젝트 업무별 매니저 리스트
	@GetMapping("project/projectmgrlist/{jobNo}")
	@ResponseBody
	public List<ProjectVO> projectmgrInfo(@PathVariable int jobNo) {
		 System.err.println(jobNo);
		return pjService.projectMgrListInfo(jobNo);
	}

	// 프로젝트 상세업무 수정 요청 처리
	@PostMapping("/project/projectupdatestatus/{pdwNo}")
	@ResponseBody
	public Map<String, Object> updatestatusProject(@PathVariable String pdwNo, @RequestBody ProjectVO projectVO) {
		Map<String, Object> response = new HashMap<>();
		try {
			//System.err.println(projectVO);
			pjService.updatestatusProject(projectVO);
			response.put("message", "수정 완료");
			response.put("status", "success");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("message", "수정 실패: " + e.getMessage());
			response.put("status", "error");
		}
		return response;
	}

	// 등록된 깃의 clone_url값을 받아와서 git 주소로 입력하기.
	@PostMapping("/project/projGitUrlAdd")
	@ResponseBody
	public boolean addGitUrl(@RequestBody ProjectVO projectVO) {
		pjService.addGitUrl(projectVO);
		return true;
	}

	// 프로젝트 gitURL을 기준으로 내 로컬 경로에 클론하기
	@PostMapping("/project/gitClone")
	@ResponseBody
	public String cloneRepository(@RequestBody ProjectVO projectVO) throws InterruptedException {
		String cloneGitUrl = projectVO.getProjectGitUrl();
		String localPath = projectVO.getCloneLocalPath();
		
		//ProjectVO gitInfo = pjService.compGitInfo();

		try {
			//Process process = Runtime.getRuntime().exec(new String[] { "git", "clone", cloneGitUrl, localPath });
			Process process = Runtime.getRuntime().exec(new String[] { "git", "clone", cloneGitUrl, localPath });
			process.waitFor();

			// 오류 로그 출력
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			StringBuilder errorLog = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				errorLog.append(line).append("\n");
			}
			if (errorLog.length() > 0) {
				System.err.println("Git Clone Error: " + errorLog);
				return "Failed to clone repository: " + errorLog.toString();
			}

			return "Repository cloned successfully!";
		} catch (IOException e) {
			e.printStackTrace();
			return "Failed to clone repository: " + e.getMessage();
		}
	}
	
	// 프로젝트 상세업무 코멘트 삭제
	@DeleteMapping("project/projectcomtsdel/{pdwcNo}")
	@ResponseBody
	public String deletedcomts(@PathVariable int pdwcNo) {
		pjService.projectcomtsDelete(pdwcNo);
		return "삭제 완료";
	}
	
	// 프로젝트 상세업무 삭제(단건)
	@DeleteMapping("project/projectdwrkdelete/{pdwNo}")
	@ResponseBody
	public String dwrkdelete(@PathVariable int pdwNo) {
		pjService.projectdeteilworkDelete(pdwNo);
		return "삭제 완료";
	}


	// 프로젝트 리스트 출력
	@GetMapping("project/projectSourceList")
	public String projectSourceList(Model model, HttpSession session) {
		Integer empNo = (Integer) session.getAttribute("userEmpNo");
		List<ProjectVO> list = pjService.projectList();
		List<ProjectTempVO> templist = pjtempService.projecttempList();
		List<ProjectVO> emplist = pjService.empList();
		ProjectVO gitInfo = pjService.compGitInfo();
		
		model.addAttribute("empNo", empNo);
		model.addAttribute("projects", list);
		model.addAttribute("templist", templist);
		model.addAttribute("emp", emplist);
		model.addAttribute("gitInfo", gitInfo);
		return "project/projectSourceList";
	}
	
	@GetMapping("project/projectSourceInfo")
	public String projectSourceInfo(@RequestParam int projNo, Model model, HttpSession session) {
		Integer empNo = (Integer) session.getAttribute("userEmpNo");
		ProjectVO projInfo = pjService.projectInfo(projNo);
		ProjectVO gitInfo = pjService.compGitInfo();
		
		model.addAttribute("empNo", empNo);
		model.addAttribute("projInfo", projInfo);
		model.addAttribute("gitInfo", gitInfo);
		return "project/projectSourceInfo";
	}
}
