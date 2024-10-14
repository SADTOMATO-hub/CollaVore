package com.collavore.app.hrm.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.hrm.service.MemberService;
import com.collavore.app.hrm.service.MemberVO;

@Controller
public class MemberController {

	private MemberService memberService;

	@Autowired
	MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	// 사원 전체조회(관리자)
	@GetMapping("memberList")
	public String memberList(Model model) {
		List<MemberVO> list = memberService.memberList();
		model.addAttribute("members", list);
		return "member/list";
	}

	// 사원 단건조회
	@GetMapping("memberInfo")
	public String memberInfo(MemberVO memberVO, Model model) {
		MemberVO findVO = memberService.memberInfo(memberVO);
		model.addAttribute("member", findVO);
		return "member/info";
	}

	// 사원 등록
	@GetMapping("memberInsert")
	public String memberInsertForm() {
		return "member/insert";
	}

	// 사원 등록 처리
	@PostMapping("memberInsert")
	public String memberInsertProcess(MemberVO memberVO) {
		int eno = memberService.memberInsert(memberVO);

		String url = null;

		if (eno > -1) {
			url = "redirect:memberInfo?empNo=" + eno;
		} else {
			url = "redirect:memberList";
		}
		return url;
	}

	// 사원 수정
	@GetMapping("memberUpdate")
	public String deptUpdateForm(MemberVO memberVO, Model model) {
		MemberVO findVO = memberService.memberInfo(memberVO);
		model.addAttribute("member", findVO);
		return "member/update";
	}

	// 사원 수정 처리
	@PostMapping("memberUpdate")
	@ResponseBody // AJAX
	public Map<String, Object> empUpdateAJAXJSON(@RequestBody MemberVO memberVO) {
		return memberService.memberUpdate(memberVO);
	}

	// 사원 삭제
	@GetMapping("memberDelete")
	public String memberDelete(Integer eno) {
		memberService.memberDelete(eno);
		return "redirect:memberList";
	}

}
