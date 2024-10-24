package com.collavore.app.approvals.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.approvals.service.ApprovalsService;
import com.collavore.app.approvals.service.ApprovalsVO;
import com.collavore.app.approvals.service.ApprovalstempVO;
import com.collavore.app.hrm.service.HrmVO;

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
	@Transactional
	public String approvalTemplateList(Model model) {
		List<ApprovalstempVO> templateInfo = approvalsService.apprTempList();
		model.addAttribute("tempInfo", templateInfo);
		return "approvals/templateList";
	}

	// 템플릿 상세 페이지
	@GetMapping("/tempInfo")
	@Transactional
	public String tmepInfo(ApprovalstempVO apprVO, Model model) {
		ApprovalstempVO tempInfo = approvalsService.apprInfo(apprVO);
		model.addAttribute("tempInfo", tempInfo);
		return "approvals/readTemplate";
	}

	// 템플릿 생성 페이지 폼
	@GetMapping("/createTempForm")
	@Transactional
	public String createTemplatePage() {
		return "approvals/createTemplateFrom";
	}

	// 템플릿 생성 데이터를 받는 곳
	@PostMapping("/createTemp")
	@Transactional
	public String createTemplate(ApprovalstempVO apprTempVO) {
		int eatNo = approvalsService.createApprsTemp(apprTempVO);
		String url = "redirect:/approvals/tempInfo?eatNo=";
		return url + eatNo;
	}

	// 템플릿 수정 페이지
	@GetMapping("/updateTempForm")
	@Transactional
	public String updateTemplateForm(ApprovalstempVO apprTempVO, Model model) {
		ApprovalstempVO apprInfo = approvalsService.apprInfo(apprTempVO);
		model.addAttribute("apprInfo", apprInfo);
		return "approvals/updateTemplateForm";
	}

	// 템플릿 수정 데이터를 받는 곳
	@PostMapping("/updateTemp")
	@Transactional
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
	@Transactional
	public String deleteTemplate(ApprovalstempVO apprVO) {
		int eatNo = approvalsService.deleteTemplate(apprVO);
		String urlFailed = "redirect:/approvals/tempInfo?eatNo=";
		String urlSucss = "redirect:/approvals/tempList";
		if (eatNo >= 1) {
			return urlSucss;
		}
		return urlFailed + eatNo;
	}

	// 전자결재 생성 폼
	@GetMapping("/createApprForm")
	@Transactional
	public String createApprovals(Model model) {
		List<ApprovalstempVO> tempInfo = approvalsService.apprTempList();
		List<HrmVO> employeesInfo = approvalsService.employeesInfo();
		model.addAttribute("tempInfo", tempInfo);
		model.addAttribute("employeesInfo", employeesInfo);
		return "approvals/createApprovalForm";
	}
	// 전자결재 생성 시, 데이터를 받는 곳
	@PostMapping("/createAppr")
	@ResponseBody
	@Transactional
	public String createAppr(ApprovalsVO apprVO) {
		System.out.println(apprVO);
		int EaNo = approvalsService.insertApprsEaTable(apprVO); 
		if (EaNo >= 0) {
			apprVO.setEaNo(EaNo); //새로 만들어진 전자결재에 전자결재 번호를 매겨 줌
			int resultOfEar = approvalsService.insertApprsEarTable(apprVO); // 전자결재 //원래 없던 ea가 들어감
			if (resultOfEar >= 0) {
				return "redirect:/approvals/tempList";
			}
		}
		return null;
	}

	// 전자결재 템플릿 내용만 호출하는 기능
	@GetMapping("/temp")
	@ResponseBody
	@Transactional
//	public String info (ApprovalstempVO apprVO, Model model) {
	public ApprovalstempVO info(ApprovalstempVO apprVO) {
		ApprovalstempVO tempInfo = approvalsService.apprInfo(apprVO);
		// model.addAttribute("tempInfo", tempInfo.getContent());
		return tempInfo;
	}

}
