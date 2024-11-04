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
import com.collavore.app.service.HomeService;
import com.collavore.app.service.HomeVO;

import jakarta.servlet.http.HttpSession;
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
	public String createApprovals(Model model) {
		List<ApprovalstempVO> tempInfo = approvalsService.apprTempList();
		List<Map<String, Object>> employeesInfo = approvalsService.employeesInfo();
		model.addAttribute("tempInfo", tempInfo);
		model.addAttribute("employeesInfo", employeesInfo);
		return "approvals/createApprovalForm";
	}

	// 전자결재 생성 시, 데이터를 받는 곳
	@PostMapping("/createAppr")
	public String createAppr(ApprovalsVO apprVO) {
		approvalsService.insertApprsEa(apprVO);
		if (apprVO.getEaNo() >= 0) {
			int resultOfEar = approvalsService.insertApprsEar(apprVO); // 전자결재 //원래 없던 ea가 들어감
			if (resultOfEar >= 0) {
				return "redirect:/approvals/myApprList/a1";
			}
		}
		return null;
	}

	// 진행 중인 전자결재 목록
	@GetMapping("/myApprList/{approvalStatus}")
	public String myApprList(ApprovalsVO apprVO, @PathVariable String approvalStatus, Model model, HttpSession session) {
		int userEmpNo = (Integer) session.getAttribute("userEmpNo");
		apprVO.setUserEmpNo(userEmpNo);
		List<ApprovalsVO> apprList = approvalsService.myApprList(apprVO);
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
		ApprovalsVO approvals = approvalsService.approvalsInfo(apprVO);
		List<Map<String, Object>> approvers = approvalsService.approversInfo(apprVO);
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
	public String updateApprovalInfoForm(ApprovalsVO apprVO, Model model) {
		List<Map<String, Object>> employeesInfo = approvalsService.employeesInfo();
		ApprovalsVO apprInfo = approvalsService.approvalsInfo(apprVO);
		List<Map<String, Object>> approvers = approvalsService.approversInfo(apprVO);
		List<ApprovalstempVO> tempInfo = approvalsService.apprTempList();
		model.addAttribute("approvals", apprInfo);
		model.addAttribute("approvers", approvers);
		model.addAttribute("apprSize", approvers.size());
		model.addAttribute("tempInfo", tempInfo);
		model.addAttribute("employeesInfo", employeesInfo);
		return "approvals/updateApproval";
	}

	// 전결 업데이트 데이터 받는 고
	@PostMapping("/updateApprInfo")
	public String updateApprovalInfo(ApprovalsVO apprVO) {
		// 전자결재 업데이트
		approvalsService.updateApproval(apprVO);

		// eaNo 기준으로 전자결재자 전체 삭제

		// 새로 받은 전자결재자 등록
		List<ApprovalsVO> apprList = apprVO.getApprovers();
		for (ApprovalsVO approvalVO : apprList) {
			approvalVO.setEmpNo(approvalVO.getEmpNo());
			approvalVO.setSort(approvalVO.getSort());
			approvalsService.updateApprover(approvalVO);
		}
		return "redirect:/approvals/myApprList/a1";
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
			return "redirect:/approvals/myApprList/a1";
		}
		return null;
	}
}
