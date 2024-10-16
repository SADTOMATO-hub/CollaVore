package com.collavore.app.cals.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.cals.service.SchsService;
import com.collavore.app.cals.service.SchsVO;

@Controller // @Controller 대신 @RestController 사용
public class SchsController {
	private final SchsService schsService;

	// 생성자 주입
	@Autowired
	public SchsController(SchsService schsService) {
		this.schsService = schsService;
	}

	// 사이드바
	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("sidemenu", "calendar_sidebar");
	}
	
	//조회 받은거 UI로 뿌리기
	@GetMapping("/cal/schList")
	public String SchsListView() {
		return "calendar/schList";
	}
	//조회 json 뿌려주기
	@PostMapping("/sch/schList")
	@ResponseBody
	public List<SchsVO> SchsList() {
		return schsService.SchsList();
	}

    
    // 등록 
	@PostMapping("/sch/schInsert")
	public String SchsInsertProcess(SchsVO SchsVO) { 
		int bno = schsService.insertSchs(SchsVO);
		return "redirect:boardInfo?bno="+bno;	
	}
}
