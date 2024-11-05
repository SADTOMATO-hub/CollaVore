package com.collavore.app.approvals.web;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.approvals.service.ApprovalsService;
import com.collavore.app.approvals.service.ApprovalsVO;
import com.collavore.app.approvals.service.ApprovalstempVO;
import com.collavore.app.hrm.service.HrmVO;
import com.collavore.app.service.HomeService;
import com.collavore.app.service.HomeVO;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/approvals")
@RequiredArgsConstructor
public class ApprovalsController {
	private final ApprovalsService approvalsService;
	private final HomeService homeService;

	@ModelAttribute
	public void addAttributes(Model model, HttpSession session) {
		String userAdmin = (String) session.getAttribute("userAdmin");
		model.addAttribute("userAdmin", userAdmin);

		@SuppressWarnings("unchecked")
		List<String> menuAuth = (List<String>) session.getAttribute("menuAuth");
		model.addAttribute("menuAuth", menuAuth);

		List<HomeVO> employeesInfo = homeService.empList();
		model.addAttribute("employees", employeesInfo);

		model.addAttribute("sidemenu", "approvals_sidebar");
	}

	// 템플릿 리스트 페이지
	@GetMapping("/tempList")
	public String approvalTemplateList(Model model) {
		List<ApprovalstempVO> templateInfo = approvalsService.apprTempList();
		model.addAttribute("tempInfo", templateInfo);
		return "approvals/templateList";
	}

	// 템플릿 상세 페이지
	@GetMapping("/readTempInfo")
	public String tmepInfo(ApprovalstempVO apprVO, Model model) {
		ApprovalstempVO tempInfo = approvalsService.apprInfo(apprVO);
		model.addAttribute("tempInfo", tempInfo);
		return "approvals/readTemplate";
	}

	// 템플릿 생성 페이지 폼
	@GetMapping("/createTempForm")
	public String createTemplatePage() {
		return "approvals/createTemplateFrom";
	}

	// 템플릿 생성 데이터를 받는 곳
	@PostMapping("/createTemp")
	public String createTemplate(ApprovalstempVO apprTempVO) {
		int eatNo = approvalsService.createApprsTemp(apprTempVO);
		String url = "redirect:/approvals/readTempInfo?eatNo=";
		return url + eatNo;
	}

	// 템플릿 수정 페이지
	@GetMapping("/updateTempForm")
	public String updateTemplateForm(ApprovalstempVO apprTempVO, Model model) {
		ApprovalstempVO apprInfo = approvalsService.apprInfo(apprTempVO);
		model.addAttribute("apprInfo", apprInfo);
		return "approvals/updateTemplateForm";
	}

	// 템플릿 수정 데이터를 받는 곳
	@PostMapping("/updateTemp")
	public String updateTemplate(ApprovalstempVO apprTempVO) {
		int result = approvalsService.updateTemplate(apprTempVO);
		int eatNo = apprTempVO.getEatNo();
		if (result > 0) {
			String url = "redirect:/approvals/readTempInfo?eatNo=";
			return url + eatNo;
		} else {
			return "updateTemplateForm?eatNo=" + eatNo;
		}
	}

	// 템플릿 삭제 기능
	@GetMapping("/deleteTemp")
	public String deleteTemplate(ApprovalstempVO apprVO) {
		int eatNo = approvalsService.deleteTemplate(apprVO);
		String urlSucss = "redirect:/approvals/tempList";
		String urlFailed = "redirect:/approvals/tempInfo?eatNo=";
		if (eatNo >= 1) {
			return urlSucss;
		}
		return urlFailed + eatNo;
	}

	// 전자결재 생성 폼
	@GetMapping("/createApprForm")
	public String createApprovals(Model model, HrmVO hrmVO,  HttpSession session ) {
		int userEmpNo = (Integer) session.getAttribute("userEmpNo");
		List<ApprovalstempVO> tempInfo = approvalsService.apprTempList();
		//부서 테이블 조회
		List <HrmVO> depts = approvalsService.depts();
		model.addAttribute("depts", depts);
		model.addAttribute("tempInfo", tempInfo);
		model.addAttribute("userEmpNo", userEmpNo);
		return "approvals/createApprovalForm";
	}
	
	//부서가 선택되면 인사테이블을 호출
	@PostMapping("/selectEmps/{deptNo}")
	@ResponseBody
	public List<HrmVO> employeeList(@PathVariable("deptNo") int deptNo, HttpSession session){
		//인사 테이블 조회
		int userEmpNo = (Integer) session.getAttribute("userEmpNo");
		List<HrmVO> employeesInfo = approvalsService.employeesInfo(userEmpNo, deptNo);
		return employeesInfo;
	}

	// 전자결재 생성 시, 데이터를 받는 곳
	@PostMapping("/createAppr")
	public String createAppr(ApprovalsVO apprVO) {
		approvalsService.insertApprsEa(apprVO);
		if (apprVO.getEaNo() >= 0) {
			int resultOfEar = approvalsService.insertApprsEar(apprVO); // 전자결재 //원래 없던 ea가 들어감
			if (resultOfEar >= 0) {
				return "redirect:/approvals/myApprList/a5";
			}
		}
		return null;
	}

	// 진행 중인 전자결재 목록
	@GetMapping("/myApprList/{approvalStatus}")
	public String myApprList(ApprovalsVO apprVO, @PathVariable String approvalStatus, Model model,
			HttpSession session) {
		int userEmpNo = (Integer) session.getAttribute("userEmpNo");
		apprVO.setUserEmpNo(userEmpNo);
		List<ApprovalsVO> apprList = approvalsService.myApprList(apprVO);
		model.addAttribute("sessionUserEmpNo", userEmpNo);
		model.addAttribute("myApprList", apprList);
		model.addAttribute("approvalStatus", approvalStatus);
		return "approvals/onProcess";
	}

	// 문서함
	@GetMapping("/approveList/{listStatus}")
	public String approveList(ApprovalsVO apprVO, Model model, @PathVariable String listStatus, HttpSession session) {
		int userEmpNo = (Integer) session.getAttribute("userEmpNo");
		apprVO.setUserEmpNo(userEmpNo);
		List<ApprovalsVO> apprList = approvalsService.approveList(apprVO);
		model.addAttribute("approveList", apprList);
		return "approvals/approvalList";
	}

	// 전자결재 상세페이지
	@GetMapping("/readApprInfo")
	public String readapprinfo(Model model, ApprovalsVO apprVO, HttpSession session) {
	    int userEmpNo = (Integer) session.getAttribute("userEmpNo");
	    apprVO.setUserEmpNo(userEmpNo);
	    
	    // 결재 및 결재자 정보 조회
	    ApprovalsVO approvals = approvalsService.approvalsInfo(apprVO);
	    List<Map<String, Object>> approvers = approvalsService.approversInfo(apprVO);

	    // 버튼 활성화 여부 및 상태 표시 설정
	    for (int i = 0; i < approvers.size(); i++) {
	        Map<String, Object> approver = approvers.get(i);
	        
	        // 상태값 초기화
	        String approverStatus = (String) approver.get("approverStatus");
	        String displayStatus;
	        // 현재 결재자의 기본 상태 설정
	        if ("b2".equals(approverStatus)) {
	            displayStatus = "승인";
	        } else if ("b3".equals(approverStatus)) {
	            displayStatus = "반려";
	        } else {
	            displayStatus = "결재 대기"; // 기본값 설정
	        }
	        // 버튼 활성화 여부 설정
	        boolean buttonEnabled = false; // 기본적으로 비활성화
	        if (i == 0) {
	            // 첫 번째 결재자는 approverStatus가 b1일 때만 버튼 활성화
	            buttonEnabled = "b1".equals(approverStatus) && userEmpNo == ((Number) approver.get("approverEmpNo")).intValue();
	        } else {
	            // 두 번째 결재자부터는 이전 결재자들이 모두 승인(b3) 또는 반려(b2) 상태일 때만 버튼 활성화
	            boolean previousApprovedOrRejected = true;
	            for (int j = 0; j < i; j++) {
	                String previousStatus = (String) approvers.get(j).get("approverStatus");
	                if (!"b2".equals(previousStatus) && !"b3".equals(previousStatus)) {
	                    previousApprovedOrRejected = false;
	                    break;
	                }
	            }
	            buttonEnabled = previousApprovedOrRejected && "b1".equals(approverStatus) && userEmpNo == ((Number) approver.get("approverEmpNo")).intValue();
	        }
	        // 버튼이 활성화된 경우 "결재 대기" 상태 표시를 숨김
	        if (buttonEnabled && "결재 대기".equals(displayStatus)) {
	            displayStatus = ""; // 버튼이 활성화된 경우 상태를 빈 문자열로 설정
	        }
	        approver.put("buttonEnabled", buttonEnabled);
	        approver.put("displayStatus", displayStatus);
	    }
	    // 모델에 결재 정보 추가
	    model.addAttribute("approvals", approvals);
	    model.addAttribute("approvers", approvers);
	    return "approvals/readApproval";
	}

	// 결재하기
	@PostMapping("/updateAppr")
	@ResponseBody
	public String updateApprove(@RequestBody ApprovalsVO apprVO) {
		int updateApprStatus = approvalsService.updateApprStatus(apprVO);
		if (updateApprStatus > 0) {
			return "성공";
		}
		return "실패";
	}

	// 전결 업데이트 폼
	@GetMapping("/updateApprInfoForm")
	public String updateApprovalInfoForm(ApprovalsVO apprVO, Model model, HttpSession session) {
		ApprovalsVO apprInfo = approvalsService.approvalsInfo(apprVO);
		List<Map<String, Object>> approvers = approvalsService.approversInfo(apprVO);
		List<ApprovalstempVO> tempInfo = approvalsService.apprTempList();
		//부서 테이블 조회
		List <HrmVO> depts = approvalsService.depts();
		model.addAttribute("depts", depts);
		model.addAttribute("approvals", apprInfo);
		model.addAttribute("approvers", approvers);
		model.addAttribute("apprSize", approvers.size());
		model.addAttribute("tempInfo", tempInfo);
		return "approvals/updateApproval";
	}
	
	//부서가 선택되면 인사테이블을 호출 

	// 전결 업데이트 데이터 받는 곳
	@PostMapping("/updateApprInfo")
	public String updateApprovalInfo(ApprovalsVO apprVO) {
		// 전자결재 업데이트
		approvalsService.updateApproval(apprVO);
		// eaNo 기준으로 전자결재자 전체 삭제
		approvalsService.deleteApprover(apprVO.getEaNo());
		// 새로 받은 전자결재자 등록
		approvalsService.insertApprsEar(apprVO);
		return "redirect:/approvals/myApprList/a5";
	}

	// 전자결재 템플릿 내용만 호출하는 기능
	@GetMapping("/temp")
	@ResponseBody
	public ApprovalstempVO info(ApprovalstempVO apprVO) {
		ApprovalstempVO tempInfo = approvalsService.apprInfo(apprVO);
		return tempInfo;
	}

	// 전자결재 삭제
	@GetMapping("/deleteAppr")
	public String deleteAppr(ApprovalsVO apprVO) {
		approvalsService.deleteApprovals(apprVO);
		if (apprVO.getResultCode() >= 0) {
			return "redirect:/approvals/myApprList/a5";
		}
		return null;
	}
}
