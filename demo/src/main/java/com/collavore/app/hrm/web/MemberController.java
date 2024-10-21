package com.collavore.app.hrm.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.collavore.app.hrm.service.HrmVO;
import com.collavore.app.hrm.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

	private final MemberService memberService;

	@Autowired
	public MemberController(MemberService memberService) {
		this.memberService = memberService;
	}

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("sidemenu", "member_sidebar");
	}

	// 사원 전체 조회 (관리자)
	@GetMapping("memberList")
	public String memberList(Model model) {
		List<HrmVO> list = memberService.memberList();
		model.addAttribute("members", list);
		return "member/memberList";
	}

	// 로그인 후 메인 페이지로 이동
	@GetMapping("/main")
	public String mainPage(HttpSession session) {
		Integer empNo = (Integer) session.getAttribute("userEmpNo");
		if (empNo == null) {
			return "redirect:/login";
		}
		return "redirect:/memberInfo?empNo=" + empNo;
	}

	// 마이페이지로 이동
	@GetMapping("/myPage")
	public String myPage(HttpSession session) {
		Integer empNo = (Integer) session.getAttribute("userEmpNo");
		if (empNo == null) {
			return "redirect:/login";
		}
		return "redirect:/memberInfo?empNo=" + empNo;
	}

	// 사원 정보 조회
	@GetMapping("/memberInfo")
	public String memberInfo(HttpSession session, Model model) {
		Integer empNo = (Integer) session.getAttribute("userEmpNo");

		// 로그인되어 있지 않다면 로그인 페이지로 이동
		if (empNo == null) {
			return "redirect:/login";
		}

		HrmVO hrmVO = new HrmVO();
		hrmVO.setEmpNo(empNo);

		HrmVO findVO = memberService.memberInfo(hrmVO);
		if (findVO == null) {
			model.addAttribute("errorMessage", "사원 정보를 찾을 수 없습니다.");
			return "error"; // 오류 페이지로 리다이렉트
		}

		model.addAttribute("member", findVO);
		model.addAttribute("isMemberInfoPage", true); // 사이드바에서 active 클래스를 설정하기 위해 추가
		return "member/memberInfo"; // memberInfo 페이지로 이동
	}

	// 사원 등록
	@GetMapping("memberInsert")
	public String memberInsertForm() {
		return "member/memberInsert";
	}

	// 사원 등록 처리
	@PostMapping("memberInsert")
	public String memberInsertProcess(HrmVO hrmVO) {
		int eno = memberService.memberInsert(hrmVO);

		if (eno > -1) {
			return "redirect:/memberInfo?empNo=" + eno;
		} else {
			return "redirect:/memberList";
		}
	}
	
	// 사원 수정
	@GetMapping("/memberEdit")
	public String editMemberInfo(HttpSession session, Model model) {
	    Integer empNo = (Integer) session.getAttribute("userEmpNo");

	    // 로그인되어 있지 않다면 로그인 페이지로 이동
	    if (empNo == null) {
	        return "redirect:/login";
	    }

	    HrmVO hrmVO = new HrmVO();
	    hrmVO.setEmpNo(empNo);

	    // 사원 정보 조회
	    HrmVO member = memberService.memberInfo(hrmVO);

	    if (member != null) {
	        model.addAttribute("member", member);  // 모델에 "member" 객체 추가
	    } else {
	        model.addAttribute("errorMessage", "사원 정보를 찾을 수 없습니다.");
	        return "error";  // 오류 페이지로 리다이렉트
	    }

	    return "member/memberEdit";  // 템플릿 파일명
	}
	
	// 사원 수정 처리
	@PostMapping("/memberEdit")
	public String updateMemberInfo(@ModelAttribute HrmVO hrmVO, HttpSession session, Model model) {
	    // 로그인된 사원의 사원번호를 세션에서 가져옴
	    Integer empNo = (Integer) session.getAttribute("userEmpNo");

	    // 로그인되어 있지 않다면 로그인 페이지로 이동
	    if (empNo == null) {
	        return "redirect:/login";
	    }

	    // 사원번호를 VO 객체에 설정
	    hrmVO.setEmpNo(empNo);

	    // 업데이트 실행
	    int result = memberService.memberUpdate(hrmVO); // 수정 처리

	    // 수정이 완료되면 수정된 정보로 다시 memberInfo 페이지로 이동
	    if (result > 0) {
	        return "redirect:/memberInfo";  // 성공 시 정보 페이지로 리다이렉트
	    } else {
	        model.addAttribute("errorMessage", "정보 수정에 실패했습니다.");
	        return "error";  // 실패 시 오류 페이지로 이동
	    }
	}


	// 로그인 페이지
	@GetMapping("/login")
	public String loginForm() {
		return "member/login"; // 로그인 페이지로 이동
	}

	// 로그인 처리
	@PostMapping("/login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model,
			HttpSession session) {

		HrmVO user = memberService.findByEmail(email); // 이메일로 사용자 조회
		if (user != null && password.equals(user.getPassword())) {
			// 로그인 성공 시 세션에 사용자 정보 저장
			session.setAttribute("userEmail", user.getEmail());
			session.setAttribute("userEmpNo", user.getEmpNo()); // 사원 번호 저장

			return "redirect:/main"; // 로그인 후 메인 페이지로 리다이렉트
		} else {
			// 로그인 실패 시 에러 메시지 전달
			model.addAttribute("loginError", "Invalid email or password.");
			return "member/login";
		}
	}

	// 로그아웃 처리
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 세션 무효화
		return "redirect:/login"; // 로그아웃 후 로그인 페이지로 리다이렉트
	}
}