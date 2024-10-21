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

    private MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("sidemenu", "member_sidebar");
    }

    // 사원 전체조회(관리자)
    @GetMapping("memberList")
    public String memberList(Model model) {
        List<HrmVO> list = memberService.memberList();
        model.addAttribute("members", list);
        return "member/memberList";
    }

	/*
	 * // 사원 정보 조회
	 * 
	 * @GetMapping("/memberInfo") public String memberInfo(@RequestParam("empNo")
	 * int empNo, Model model, HttpSession session) { String userEmail = (String)
	 * session.getAttribute("userEmail");
	 * 
	 * if (userEmail == null) { return "redirect:/login"; // 로그인되지 않은 경우 로그인 페이지로
	 * 리다이렉트 }
	 * 
	 * HrmVO hrmVO = new HrmVO(); hrmVO.setEmpNo(empNo);
	 * 
	 * HrmVO findVO = memberService.memberInfo(hrmVO); model.addAttribute("member",
	 * findVO);
	 * 
	 * return "member/memberInfo"; // 마이페이지로 이동 }
	 */
    
    @GetMapping("/memberInfo")
    public String memberInfo(@RequestParam("empNo") int empNo, Model model, HttpSession session) {
        String userEmail = (String) session.getAttribute("userEmail");

        if (userEmail == null) {
            return "redirect:/login";  // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        HrmVO hrmVO = new HrmVO();
        hrmVO.setEmpNo(empNo);

        HrmVO findVO = memberService.memberInfo(hrmVO);
        if (findVO == null) {
            model.addAttribute("errorMessage", "사원 정보를 찾을 수 없습니다.");
            return "error";  // 오류 페이지로 리다이렉트
        }

        model.addAttribute("member", findVO);
        return "member/memberInfo";
    }


    // 사원 등록
    @GetMapping("memberInsert")
    public String memberInsertForm() {
        return "member/memberinsert";
    }

    // 사원 등록 처리
    @PostMapping("memberInsert")
    public String memberInsertProcess(HrmVO hrmVO) {
        int eno = memberService.memberInsert(hrmVO);

        if (eno > -1) {
            return "redirect:memberInfo?empNo=" + eno;
        } else {
            return "redirect:memberList";
        }
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "member/login";  // 로그인 페이지로 이동
    }
    
    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam("email") String email, 
                        @RequestParam("password") String password, 
                        Model model, HttpSession session) {

        HrmVO user = memberService.findByEmail(email);  // 이메일로 사용자 조회
        if (user != null && password.equals(user.getPassword())) {  
            // 로그인 성공 시 세션에 사용자 정보 저장
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userEmpNo", user.getEmpNo());  // 사원 번호 저장

            System.out.println("로그인 성공: " + user.getEmail());  // 디버깅용 로그 추가
            return "redirect:/memberInfo?empNo=" + user.getEmpNo();
        } else {
            // 로그인 실패 시 에러 메시지 전달
            model.addAttribute("loginError", "Invalid email or password.");
            return "member/login";
        }
    }


    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 세션 무효화
        return "redirect:/login";  // 로그아웃 후 로그인 페이지로 리다이렉트
    }
}
