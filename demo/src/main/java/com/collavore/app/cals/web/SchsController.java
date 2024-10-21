package com.collavore.app.cals.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.cals.service.CalsVO;
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

	// 조회 받은거 UI로 뿌리기
	@GetMapping("/cal/schList")
	public String SchsListView() {
		return "calendar/schList";
	}

	// 조회 json 뿌려주기
	@PostMapping("/sch/schList")
	@ResponseBody
	public List<SchsVO> SchsList() {
		return schsService.SchsList();
	}

	// 등록
	@PostMapping("/sch/schInsert")
	@ResponseBody
	public Map<String, Object> insertSchs(@RequestBody SchsVO schsVO) {
		Map<String, Object> result = new HashMap<>();
		try {
			// 받은 데이터 확인용 로그
	        System.out.println("Received Title: " + schsVO.getTitle());
	        System.out.println("Received Start Date: " + schsVO.getStartDate());
	        System.out.println("Received End Date: " + schsVO.getEndDate());
	        
	        // DB에 저장
			int id = schsService.insertSchs(schsVO);
			result.put("success", true);
			result.put("id", id);
		} catch (Exception e) {
			result.put("success", false);
		}
		return result;
	}

	// 수정
	@PostMapping("/sch/schUpdate")
	@ResponseBody
	public Map<String, Object> updateSchedule(@RequestBody SchsVO schsVO) {
		Map<String, Object> resultMap = schsService.updateSchs(schsVO);

		return resultMap;
	}

	// 삭제
	@PostMapping("/sch/schDelete")
	@ResponseBody
	public Map<String, Object> deleteSchs(@RequestBody Map<String, Integer> request) {
		Map<String, Object> result = new HashMap<>();
		try {
			schsService.deleteSchs(request.get("schNo"));
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
		}
		return result;
	}
	
//==============================END 일정관리 ===============================
	
	
	
	
	//전체조회
	@GetMapping("/cal/calList")
	@ResponseBody
	public List<CalsVO> getAllCalendars() {
	    return schsService.allCal();  // 서비스에서 DB의 캘린더 목록을 가져옴
	}
	
	
	
	// 등록
	@PostMapping("/cal/calInsert")
	@ResponseBody
	public Map<String, Object> insertCals(@RequestBody CalsVO calsVO) {
	    Map<String, Object> result = new HashMap<>();
	    try {
	        int calNo = schsService.insertCals(calsVO);  // Service를 통해 캘린더 등록
	        result.put("success", true);
	        result.put("calNo", calNo);  // 등록된 캘린더 번호 반환
	    } catch (Exception e) {
	        result.put("success", false);
	        result.put("message", "캘린더 등록에 실패했습니다.");
	    }
	    return result;
	}



}
