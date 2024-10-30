package com.collavore.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.collavore.app.service.HomeService;
import com.collavore.app.service.HomeVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	private final HomeService homeService;
	
	@Autowired
	public HomeController(HomeService homeService) {
		this.homeService = homeService;
	}
	
	// 사이드 적용
	@ModelAttribute
	public void addAttributes(Model model) {
		// 게시판목록
		List<HomeVO> hvo = homeService.selBoardList();
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
		
		return "/home";
	}
}
