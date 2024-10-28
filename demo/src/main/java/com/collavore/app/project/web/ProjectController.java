package com.collavore.app.project.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.collavore.app.common.service.FileUtill;
import com.collavore.app.project.service.PjService;
import com.collavore.app.project.service.PjTempService;
import com.collavore.app.project.service.ProjectFilesVO;
import com.collavore.app.project.service.ProjectFoldersVO;
import com.collavore.app.project.service.ProjectTempVO;
import com.collavore.app.project.service.ProjectVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProjectController {
	private final PjService pjService;
	private final PjTempService pjtempService;

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("sidemenu", "project_sidebar");
	}

	// 프로젝트 리스트 출력
	@GetMapping("project/projectlist")
	public String projectList(Model model) {
		List<ProjectVO> list = pjService.projectList();
		List<ProjectTempVO> templist = pjtempService.projecttempList();
		List<ProjectVO> emplist = pjService.empList();
		
		
		model.addAttribute("projects", list);
		model.addAttribute("templist", templist);
		model.addAttribute("emp", emplist);
		return "project/projectList";
	}

	// 프로젝트 생성 모달
	@PostMapping("project/projectinsert")
	@ResponseBody
	public Map<String, Object> insertAjax(ProjectVO projectVO) {
		Map<String, Object> map = new HashMap<>();
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

	// 파일 업로드 처리 메소드
	@PostMapping("/project/uploadfile")
	public String uploadFile(@RequestPart("file") MultipartFile file, 
			                 @ModelAttribute ProjectFilesVO ProjectFilesVO,
			                 Model model) {
		if (file.isEmpty()) {
			model.addAttribute("message", "파일이 비어 있습니다.");
			return "redirect:/project/projectfilelist"; // 파일이 비어 있으면 목록으로 리다이렉트
		}

		try {
			// 파일 저장 경로 설정
			String uploadDir = "fileuploads/"; // 실제 파일이 저장될 경로 설정
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
//		List<ProjectVO> projmgr = pjService.projMgrInfo();
//		List<ProjectVO> wrkmgr = pjService.wrkMgrIngo();
		/* List<ProjectVO> dwrkmgr = pjService.dwrkMgrInfo(); */
		
//		System.err.println(list);

		model.addAttribute("projects", list);
		model.addAttribute("jobs", jobs);
		model.addAttribute("dept", departments);
//		model.addAttribute("projmgriist", projmgr);
//		model.addAttribute("wrkmgriist", wrkmgr);
//		model.addAttribute("dwrkmgrlist", dwrkmgr);
		return "project/projectWorkList";
	}

	// 프로젝트 업무 모달
	@PostMapping("project/projectwrkinsert")
	@ResponseBody
	public Map<String, Object> insertwrkAjax(ProjectVO projectVO) {
		Map<String, Object> map = new HashMap<>();
//			 System.err.println(projectVO);
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
						System.err.println(projectVO);
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
		
				// 상세업무 코멘트 단건 리스트
				@GetMapping("/project/projecdwrkcomtstinfo/{pdwNo}")
				@ResponseBody
				public List<ProjectVO> projectDWrkComtInfo(@PathVariable int pdwNo) {
				    return pjService.projectDWrkComtInfo(pdwNo);
				}
				

				// 상세업무 코멘트 생성
				@PostMapping("project/projectdwrkcomtinsert")
				@ResponseBody
				public Map<String, Object> dwrkcomtinsertAjax(@RequestBody ProjectVO projectVO) {
					Map<String, Object> map = new HashMap<>();
					System.err.println(projectVO);
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
				
}
