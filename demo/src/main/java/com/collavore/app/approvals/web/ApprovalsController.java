package com.collavore.app.approvals.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collavore.app.approvals.service.ApprovalsService;
import com.collavore.app.approvals.service.ApprovalstempVO;


@RestController
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
	@GetMapping("approvals/tempList")
	public String approvalTemplateList(Model model) {
		List<ApprovalstempVO> templateInfo = approvalsService.apprTempList();
		model.addAttribute("tempInfo", templateInfo);
		return "approvals/templateList";
	}
	//템플릿 상세 페이지
	@GetMapping("approvals/tempInfo")
	public String tmepInfo(ApprovalstempVO eatNo, Model model) {
		ApprovalstempVO tempInfo = approvalsService.apprInfo(eatNo);
		model.addAttribute("tempInfo", tempInfo);
		return "approvals/readTemplate";
	}
	//템플릿 생성 페이지 폼
	@GetMapping("approvals/createTempForm")
	 public String createTemplatePage() {
		return "approvals/createTemplateFrom";
	}
	//템플릿 생성 데이터를 받는 곳
	@PostMapping("approvals/createTemp")
	public String createTemplate(ApprovalstempVO apprTempVO) {
		int eatNo = approvalsService.createApprsTemp(apprTempVO);
		String url = "redirect:/approvals/tempInfo?eatNo=";
		return url + eatNo;
	}
	//템플릿 삭제 기능
	
}
