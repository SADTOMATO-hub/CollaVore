package com.collavore.app;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
		
		// 내 담당 프로젝트 상세업무 출력
		List<HomeVO> todos = homeService.selTodoList(empNo);
		model.addAttribute("todos", todos);
		
		// 상단 통계
		Map<String, Object> stats = homeService.setStatInfo();
		model.addAttribute("stats", stats);
		
		return "home";
	}
	
	// 부서 조회
	@GetMapping("/menuDeptList")
	@ResponseBody
	public List<HomeVO> deptList(){
		List<HomeVO> deptList = homeService.deptAuthList();
		return deptList;
	}
	
	// 메뉴별 사원 조회
	@GetMapping("/empList")
	@ResponseBody
	public List<HomeVO> empList(HomeVO homeVO){
		List<HomeVO> employeesInfo = homeService.empAuthList(homeVO);
		return employeesInfo;
	}
	
	// 메뉴별 사원 조회
	@GetMapping("/hasMenuAuth")
	@ResponseBody
	public List<HomeVO> hasMenuAuth(HomeVO homeVO){
		List<HomeVO> menuEmpList = homeService.selMenuAuthEmpList(homeVO);
		return menuEmpList;
	}
	
	// 사원에게 메뉴 권한 부여
	@PostMapping("/giveMenuAuth")
	@ResponseBody
	public HomeVO giveMenuAuth(@RequestBody HomeVO homeVO, HttpSession session) {
		homeService.giveMenuAuth(homeVO);
		
        Integer empNo = (Integer) session.getAttribute("userEmpNo");

		List<String> menuAuth = userDetailsService.myMenuAuth(empNo);
		session.setAttribute("menuAuth", menuAuth);
        
		HomeVO menuEmpList = homeService.selMenuAuthEmpInfo(homeVO);
		return menuEmpList;
	}
	
	// 사원에게 메뉴 권한 삭제
	@DeleteMapping("/removeMenuAuth/{authNo}")
	@ResponseBody
	public boolean removeMenuAuth(@PathVariable Integer authNo, HttpSession session) {
		int result = homeService.removeMenuAuth(authNo);
        Integer empNo = (Integer) session.getAttribute("userEmpNo");

		List<String> menuAuth = userDetailsService.myMenuAuth(empNo);
		session.setAttribute("menuAuth", menuAuth);
		return result > 0 ? true : false;
	}
	
}
