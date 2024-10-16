package com.collavore.app.cals.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.collavore.app.cals.service.SchsService;
import com.collavore.app.cals.service.SchsVO;

@RestController
@RequestMapping("/api/schs")
public class SchsController {
	private SchsService schsService;

	@Autowired
	public void setSchsService(SchsService schsService) {
		this.schsService = schsService;
	}

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("sidemenu", "calendar_sidebar");
	}

	@GetMapping("/list")
	@ResponseBody
	public List<SchsVO> getAllSchedules() {
		return schsService.schList();
	}

	@PostMapping("/add")
	@ResponseBody
	public String addSchedule(@RequestBody SchsVO schsVO) {
		int result = schsService.insertSch(schsVO);
		if (result > 0) {
			return "일정이 성공적으로 등록되었습니다!";
		} else {
			return "일정 등록에 실패했습니다.";
		}
	}

}
//@GetMapping("/sch/schList")
//public String cal() {
//	return "calendar/schList";
//}// end homepage
