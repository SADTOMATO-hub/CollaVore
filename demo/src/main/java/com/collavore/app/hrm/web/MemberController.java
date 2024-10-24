package com.collavore.app.hrm.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.collavore.app.common.service.PageDTO;
import com.collavore.app.hrm.service.DeptService;
import com.collavore.app.hrm.service.HrmVO;
import com.collavore.app.hrm.service.JobService;
import com.collavore.app.hrm.service.MemberService;
import com.collavore.app.hrm.service.PosiService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

	private final MemberService memberService;
	private final DeptService deptService;
	private final JobService jobService;
	private final PosiService posiService;

	@Autowired
	public MemberController(MemberService memberService, DeptService deptService, JobService jobService,
			PosiService posiService) {
		this.memberService = memberService;
		this.deptService = deptService;
		this.jobService = jobService;
		this.posiService = posiService;
	}

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("sidemenu", "member_sidebar");
	}

	// 로그인 페이지
	@GetMapping("/member/login")
	public String loginForm() {
		return "member/login"; // 로그인 페이지로 이동
	}

	// 로그인 처리
	@PostMapping("/member/login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model,
			HttpSession session) {

		HrmVO user = memberService.findByEmail(email); // 이메일로 사용자 조회
		if (user == null) {
			// 이메일이 등록되지 않은 경우
			model.addAttribute("loginError", "등록되지 않은 이메일입니다. 관리자에게 문의 해주세요.");
			return "member/login"; // 로그인 화면으로 다시 이동
		}

		if (!password.equals(user.getPassword())) {
			// 비밀번호가 틀린 경우
			model.addAttribute("loginError", "비밀번호가 올바르지 않습니다.");
			return "member/login"; // 로그인 화면으로 다시 이동
		}

		// 로그인 성공 시 세션에 사용자 정보 저장
		session.setAttribute("userEmail", user.getEmail());
		session.setAttribute("userEmpNo", user.getEmpNo()); // 사원 번호 저장
		return "redirect:/main"; // 로그인 후 메인 페이지로 리다이렉트
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

	// 로그아웃 처리
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 세션 무효화
		return "redirect:/login"; // 로그아웃 후 로그인 페이지로 리다이렉트
	}

	// 사원 정보 조회
	@GetMapping("/memberInfo")
	public String memberInfo(HttpSession session, Model model) {
		// 세션에서 사번을 문자열로 가져옴
		Integer empNo = (Integer) session.getAttribute("userEmpNo");

		// 로그인되어 있지 않다면 로그인 페이지로 이동
		if (empNo == null) {
			return "redirect:/login";
		}

		// HrmVO 객체에 사번 설정
		HrmVO hrmVO = new HrmVO();
		hrmVO.setEmpNo(empNo);

		// 사원 정보 조회
		HrmVO findVO = memberService.memberInfo(hrmVO);
		if (findVO == null) {
			model.addAttribute("errorMessage", "사원 정보를 찾을 수 없습니다.");
			return "error"; // 오류 페이지로 이동
		}

		model.addAttribute("member", findVO);
		model.addAttribute("isMemberInfoPage", true); // 사이드바에서 active 클래스를 설정하기 위해 추가
		return "member/memberInfo"; // memberInfo 페이지로 이동
	}

	// 사원 수정
	@GetMapping("/memberEdit")
	public String editMemberInfo(HttpSession session, Model model) {
		// 세션에서 사번을 문자열로 가져옴
		Integer empNo = (Integer) session.getAttribute("userEmpNo");

		// 로그인되어 있지 않다면 로그인 페이지로 이동
		if (empNo == null) {
			return "redirect:/login";
		}

		// HrmVO 객체에 사번 설정
		HrmVO hrmVO = new HrmVO();
		hrmVO.setEmpNo(empNo);

		// 사원 정보 조회
		HrmVO member = memberService.memberInfo(hrmVO);

		if (member != null) {
			model.addAttribute("member", member); // 모델에 "member" 객체 추가
		} else {
			model.addAttribute("errorMessage", "사원 정보를 찾을 수 없습니다.");
			return "error"; // 오류 페이지로 이동
		}

		return "member/memberEdit"; // 템플릿 파일명
	}

	// 사원 수정 처리
	@PostMapping("/memberEdit")
	public String updateMemberInfo(@ModelAttribute HrmVO hrmVO, HttpSession session) {
		Integer empNo = (Integer) session.getAttribute("userEmpNo");
		System.out.println("입력된 비밀번호: " + hrmVO.getPassword());
		if (empNo == null) {
			return "redirect:/login";
		}

		HrmVO originalMember = memberService.memberInfo(hrmVO);

		// null이나 빈 값이 넘어오면 원래 데이터를 유지
		if (hrmVO.getInfo() == null || hrmVO.getInfo().isEmpty()) {
			hrmVO.setInfo(originalMember.getInfo());
		}

		if (hrmVO.getEmpName() == null || hrmVO.getEmpName().isEmpty()) {
			hrmVO.setEmpName(originalMember.getEmpName());
		}

		if (hrmVO.getTel() == null || hrmVO.getTel().isEmpty()) {
			hrmVO.setTel(originalMember.getTel());
		}

		if (hrmVO.getPassword() == null || hrmVO.getPassword().isEmpty()) {
			hrmVO.setPassword(originalMember.getPassword());
		}

		// 이후에 업데이트 처리
		memberService.memberUpdate(hrmVO);

		return "redirect:/memberInfo";
	}

	// 관리자 영역
	// ────────────────────────────────────────────────────────────────────────────────────────────────────
	// 사원 전체 조회 (관리자)
	@GetMapping("/memberList")
	public String selectMemberAll(HrmVO hrmVO, Model model) {
		String page = hrmVO.getPage() == null ? "1" : hrmVO.getPage();
		
		int totalCnt = memberService.totalListCnt();
		PageDTO pageing = new PageDTO(page, 15, totalCnt);
		model.addAttribute("pageing", pageing);
		
		List<HrmVO> memberList = memberService.selectMemberAll(page);
		model.addAttribute("members", memberList);
		
		return "member/memberList"; // 뷰 파일 반환
	}


	// 사원 등록 폼 이동 (관리자)
	@GetMapping("/memberInsert")
	public String memberInsertForm(Model model) {
	    // 빈 hrmVO 객체를 초기화하여 전달
	    model.addAttribute("hrmVO", new HrmVO());

	    // 사번 자동 생성
	    Integer empNo = memberService.generateEmpNo();
	    model.addAttribute("empNo", empNo);

	    // 부서 목록을 가져와서 모델에 추가
	    List<HrmVO> departments = deptService.getExistingDepts();
	    model.addAttribute("departments", departments);

	    // 직위 목록을 가져와서 모델에 추가
	    List<HrmVO> positions = posiService.getExistingPositions();
	    model.addAttribute("positions", positions);

	    // 직무 목록을 가져와서 모델에 추가
	    List<HrmVO> jobs = jobService.getExistinJobs();
	    model.addAttribute("jobs", jobs);

	    return "member/memberInsert"; // 명확하게 "member/memberInsert" 경로로 반환
	}

	// 사원 등록 처리 (관리자)
	@PostMapping("/memberInsert")
	public String memberInsert(@ModelAttribute("hrmVO") HrmVO hrmVO, Model model) {
	    boolean hasErrors = false;

	    // 연락처 중복 검사
	    if (memberService.isTelDuplicate(hrmVO.getTel())) {
	        model.addAttribute("telDuplicateError", "해당 연락처는 이미 등록되어 있습니다.");
	        hasErrors = true;
	    }

	    // 이메일 중복 검사
	    if (memberService.isEmailDuplicate(hrmVO.getEmail())) {
	        model.addAttribute("emailDuplicateError", "해당 이메일은 이미 등록되어 있습니다.");
	        hasErrors = true;
	    }

	    // 오류가 있으면 입력 페이지로 다시 돌아가고, 입력된 데이터를 유지
	    if (hasErrors) {
	        model.addAttribute("hrmVO", hrmVO);  // 입력된 값 유지
	        return "member/memberInsert";  // 입력 페이지로 유지
	    }

	    // 사원 등록 처리
	    try {
	        int result = memberService.insertMember(hrmVO);
	        if (result > 0) {
	            model.addAttribute("message", "사원이 성공적으로 등록되었습니다.");
	            return "redirect:/memberList";  // 성공 시 리스트 페이지로 이동
	        } else {
	            model.addAttribute("errorMessage", "사원 등록에 실패했습니다.");
	            return "member/memberInsert";  // 실패 시 입력 페이지로 유지
	        }
	    } catch (Exception e) {
	        model.addAttribute("errorMessage", "처리 중 오류가 발생했습니다.");
	        return "member/memberInsert";  // 예외 발생 시 입력 페이지로 유지
	    }
	}

	
	

	@GetMapping("/memberUpdate")
	public String updateMemberForm(@RequestParam("empNo") Integer empNo, Model model) {
		// 사원 정보 조회
		HrmVO member = memberService.memberInfoByEmpNo(empNo);
		model.addAttribute("member", member);

		// 부서, 직위, 직무 목록을 가져와서 모델에 추가
		List<HrmVO> departments = deptService.getExistingDepts();
		List<HrmVO> positions = posiService.getExistingPositions();
		List<HrmVO> jobs = jobService.getExistinJobs();
		model.addAttribute("departments", departments);
		model.addAttribute("positions", positions);
		model.addAttribute("jobs", jobs);

		return "member/memberUpdate"; // memberUpdate 뷰 파일로 이동
	}


	@PostMapping("/memberUpdate")
	public String updateMember(HrmVO hrmVO, Model model, RedirectAttributes redirectAttributes) {
		int result = memberService.updateMemberByAdmin(hrmVO); // 사원 정보 수정 처리

		if (result > 0) {
			// 수정된 사원 정보를 다시 조회하여 모델에 담기
			HrmVO updatedMember = memberService.getMemberById(hrmVO.getEmpNo());
			model.addAttribute("member", updatedMember); // 수정된 데이터를 모델에 추가

			// 성공 메시지 추가
			model.addAttribute("message", "사원이 성공적으로 수정되었습니다.");

			// 수정이 완료된 후 같은 화면으로 이동하게 설정
			return "redirect:/memberUpdate?empNo=" + hrmVO.getEmpNo(); // 업데이트된 내용을 다시 표시
		} else {
			// 수정 실패 메시지 설정
			redirectAttributes.addFlashAttribute("message", "사원 수정에 실패했습니다.");
			return "redirect:/memberList"; // 실패 시 리스트 페이지로 리다이렉트
		}
	}

	@GetMapping("/checkDuplicateTel")
	public ResponseEntity<Map<String, Boolean>> checkDuplicateTel(@RequestParam("tel") String tel) {
		boolean isDuplicate = memberService.isTelDuplicate(tel);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isTelDuplicate", isDuplicate);
		return ResponseEntity.ok(response);
	}

	// 이메일 중복 확인 API
	@GetMapping("/checkDuplicateEmail")
	public ResponseEntity<Map<String, Boolean>> checkDuplicateEmail(@RequestParam("email") String email) {
		boolean isDuplicate = memberService.isEmailDuplicate(email);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isEmailDuplicate", isDuplicate);
		return ResponseEntity.ok(response);
	}

	// 사원 삭제 처리 (관리자)
	@PostMapping("/deleteMember")
	public ResponseEntity<Void> deleteMember(@RequestParam("empNo") Integer empNo) {
		try {
			memberService.deleteMember(empNo); // 사원 삭제 서비스 호출
			return ResponseEntity.ok().build(); // 성공 시 OK 응답
		} catch (Exception e) {
			// 에러 발생 시 에러 응답 반환
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}