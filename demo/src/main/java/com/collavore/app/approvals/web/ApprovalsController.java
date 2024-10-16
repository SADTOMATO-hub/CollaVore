package com.collavore.app.approvals.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.approvals.service.ApprovalsService;
import com.collavore.app.approvals.service.ApprovalstempVO;


@Controller
public class ApprovalsController {
	private ApprovalsService approvalsService;
	
	@Autowired
	ApprovalsController(ApprovalsService approvalsService){
		this.approvalsService = approvalsService;
	}
	@ModelAttribute
	public void addAttributes(Model model) {
	    model.addAttribute("sidemenu", "approvals_sidebar");
	}
	//템플릿 리스트 페이지
	@GetMapping("/approvals/tempList")
	public String approvalTemplateList(Model model) {
		List<ApprovalstempVO> templateInfo = approvalsService.apprTempList();
		model.addAttribute("tempInfo", templateInfo);
		return "approvals/templateList";
	}
	//템플릿 상세 페이지
	@GetMapping("/approvals/tempInfo")
	public String tmepInfo(ApprovalstempVO apprVO, Model model) {
		ApprovalstempVO tempInfo = approvalsService.apprInfo(apprVO);
		model.addAttribute("tempInfo", tempInfo);
		return "approvals/readTemplate";
	}
	//템플릿 생성 페이지 폼
	@GetMapping("/approvals/createTempForm")
	 public String createTemplatePage() {
		return "approvals/createTemplateFrom";
	}
	//템플릿 생성 데이터를 받는 곳
	@PostMapping("/approvals/createTemp")
	public String createTemplate(ApprovalstempVO apprTempVO) {
		int eatNo = approvalsService.createApprsTemp(apprTempVO);
		String url = "redirect:/approvals/tempInfo?eatNo=";
		return url + eatNo;
	}
	//템플릿 수정 페이지
	@GetMapping("/approvals/updateTempForm")
	public String updateTemplateForm(ApprovalstempVO apprTempVO, Model model) {
		ApprovalstempVO apprInfo = approvalsService.apprInfo(apprTempVO);
		model.addAttribute("apprInfo", apprInfo);
		return "approvals/updateTemplateForm";
	}
	//템플릿 수정 데이터를 받는 곳
	@PostMapping("/approvals/updateTemp")
	public String updateTemplate(ApprovalstempVO apprTempVO){
		int result =  approvalsService.updateTemplate(apprTempVO);
		if (result > 0) {
			int eatNo = apprTempVO.getEatNo();	
			String url = "redirect:/approvals/tempInfo?eatNo=";
			return url + eatNo;
		}else {
			return null;			
		}
	}
	//템플릿 삭제 기능
	@GetMapping("/approvals/deleteTemp")
	public String deleteTemplate( ApprovalstempVO apprVO) {
		int eatNo = approvalsService.DeleteTemplate(apprVO);
		String urlFailed = "redirect:/approvals/tempInfo?eatNo=";
		String urlSucss = "redirect:/approvals/tempList";
		if (eatNo >= 1) {
			return urlSucss;
		}
		return urlFailed + eatNo;
	}
}
