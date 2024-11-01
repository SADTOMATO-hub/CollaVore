package com.collavore.app;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.collavore.app.security.service.EmpVO;
import com.collavore.app.security.service.impl.UserDetailsService;
import com.collavore.app.service.HomeService;
import com.collavore.app.service.HomeVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	private final HomeService homeService;
	private final UserDetailsService userDetailsService;
	
	// 사이드 적용
	@ModelAttribute
	public void addAttributes(Model model, HttpSession session) {
		List<EmpVO> employeesInfo = userDetailsService.empList();
		model.addAttribute("employees", employeesInfo);
		
		Integer userGrade = (Integer) session.getAttribute("userGrade");
		model.addAttribute("userGrade", userGrade);

		String userAdmin = (String) session.getAttribute("userAdmin");
		model.addAttribute("userAdmin", userAdmin);

		@SuppressWarnings("unchecked")
		List<String> menuAuth = (List<String>) session.getAttribute("menuAuth");
		model.addAttribute("menuAuth", menuAuth);
		
		// 게시판목록
		List<HomeVO> hvo = homeService.selBoardList(userGrade);
		model.addAttribute("boards", hvo);
	    model.addAttribute("sidemenu", "default_sidebar");
	}
	
	@GetMapping("/dashboard")
	public String homePage(Model model, HttpSession session) {
        Integer empNo = (Integer) session.getAttribute("userEmpNo");
		
        // 공지사항게시글조회
        List<HomeVO> notices = homeService.selNoticeList();
		model.addAttribute("notices", notices);
		
		// 내가 기안한 전자결재
		List<HomeVO> apps = homeService.selAppList(empNo);
		model.addAttribute("apps", apps);
		
		return "home";
	}
}
