package com.collavore.app.approvals.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.approvals.service.ApprovalsService;
import com.collavore.app.approvals.service.ApprovalstempVO;
import com.collavore.app.approvals.service.ApproversVO;

@Controller
@RequestMapping("/approvals")
public class ApprovalsController {
	private ApprovalsService approvalsService;

	@Autowired
	ApprovalsController(ApprovalsService approvalsService) {
		this.approvalsService = approvalsService;
	}

	@ModelAttribute
	public void addAttributes(Model model) {
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
	@GetMapping("/tempInfo")
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
		String url = "redirect:/approvals/tempInfo?eatNo=";
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
		if (result > 0) {
			int eatNo = apprTempVO.getEatNo();
			String url = "redirect:/approvals/tempInfo?eatNo=";
			return url + eatNo;
		} else {
			return null;
		}
	}

	// 템플릿 삭제 기능
	@GetMapping("/deleteTemp")
	public String deleteTemplate(ApprovalstempVO apprVO) {
		int eatNo = approvalsService.DeleteTemplate(apprVO);
		String urlFailed = "redirect:/approvals/tempInfo?eatNo=";
		String urlSucss = "redirect:/approvals/tempList";
		if (eatNo >= 1) {
			return urlSucss;
		}
		return urlFailed + eatNo;
	}

	// 전자결재 폼 생성
	@GetMapping("/createApprForm")
	public String createApprovals(Model model) {
		List<ApprovalstempVO> tempInfo = approvalsService.apprTempList();
		List<ApproversVO> apprvers = approvalsService.approversData();
		model.addAttribute("tempInfo", tempInfo);
		model.addAttribute("apprvers", apprvers);
		return "approvals/createApprovalForm";
	}
	// 전자결재 데이터를 받는 곳
	// @PostMapping("/createAppr")

	// 전자결재 템플릿 내용만 호출하는 기능
	@GetMapping("/temp")
	@ResponseBody
//	public String info (ApprovalstempVO apprVO, Model model) {
	public ApprovalstempVO info(ApprovalstempVO apprVO) {
		ApprovalstempVO tempInfo = approvalsService.apprInfo(apprVO);
		// model.addAttribute("tempInfo", tempInfo.getContent());
		return tempInfo;
	}
}
