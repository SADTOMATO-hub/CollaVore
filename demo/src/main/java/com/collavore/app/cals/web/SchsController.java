package com.collavore.app.cals.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.cals.service.SchsService;
import com.collavore.app.cals.service.SchsVO;

import jakarta.servlet.http.HttpSession;

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
	
	
	
//	@PostMapping("/sch/schList")
//	@ResponseBody
//	public List<SchsVO> getCalendarEvents(@RequestBody Map<String, Object> params,HttpSession session) {
//	    String calType = (String) params.get("calType");
//	    Integer empNo = (Integer) session.getAttribute("userEmpNo");
//	    
//	    if ("g1".equals(calType)) {
//	        return schsService.soloCal(empNo); // 개인 캘린더(g1 타입) 일정만
//	    } else if ("g2".equals(calType)) {
//	        return schsService.teamCal(empNo); // 공유 캘린더(g2 타입) 일정만
//	    } else {
//	        return schsService.SchsList(empNo); // 전체 일정
//	    }
//	}
	
//	// 캘린더 이벤트 조회
//    @PostMapping("/sch/schList")
//    public List<SchsVO> getCalendarEvents(@RequestBody Map<String, Object> params, HttpSession session) {
//        Integer empNo = (Integer) session.getAttribute("userEmpNo");
//        return schsService.getCalendarEvents(empNo, params);
//    }
	
	// 조회 json 뿌려주기
	@PostMapping("/sch/schList")
	@ResponseBody
	public List<SchsVO> getSchsList(HttpSession session) {
		Integer empNo = (Integer) session.getAttribute("userEmpNo");
		return schsService.SchsList(empNo);
	}

	// 단건 조회 API
	@GetMapping("/sch/schInfo")
	public ResponseEntity<Map<String, Object>> getEventById(@RequestParam Integer schNo) {
		Map<String, Object> result = new HashMap<>();
		try {
			// SchsVO 객체에 schNo 설정
			SchsVO schsVO = new SchsVO();
			schsVO.setSchNo(schNo);

			// 단건 조회
			SchsVO event = schsService.SchsInfo(schsVO);

			if (event != null) {
				result.put("success", true);
				result.put("data", event);
			} else {
				result.put("success", false);
				result.put("message", "일정을 찾을 수 없습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("message", "일정 조회 중 오류가 발생했습니다.");
		}
		return ResponseEntity.ok(result);
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
			System.out.println("Received calNo: " + schsVO.getCalNo());

			// DB에 일정 저장
			int id = schsService.insertSchs(schsVO);

			if (id > 0) {
				// 일정 등록 성공 시 sch_no 값을 설정
				schsVO.setSchNo(id);

				// 알림 여부 체크 후 알림 데이터 삽입
				if ("f1".equals(schsVO.getIsAlarm())) { // 알림 사용 여부가 f1인 경우
					schsVO.setAlarmRegDate(new Date()); // 현재 날짜로 등록

					// 알림 데이터 DB에 삽입
					schsService.insertAlarm(schsVO);
				}

				System.out.println("Inserted schedule with ID: " + id); // 성공 로그
				result.put("success", true);
				result.put("id", id);
			} else {
				System.out.println("일정 등록에 실패했습니다.");
				result.put("success", false);
			}
		} catch (Exception e) {
			System.out.println("Error inserting schedule: " + e.getMessage());
			e.printStackTrace();
			result.put("success", false);
		}
		return result;
	}

	// 수정
	@PostMapping("/sch/schUpdate")
	@ResponseBody
	public Map<String, Object> updateSchedule(@RequestBody SchsVO schsVO) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// 일정 및 알림 정보를 업데이트하고, 결과 메시지 포함된 맵을 반환받음
			resultMap = schsService.updateSchs(schsVO);

			// 성공 여부에 따른 메시지 및 처리 결과 반환
			if (Boolean.TRUE.equals(resultMap.get("result"))) {
				resultMap.put("success", true);
				resultMap.put("message", "일정 및 알림 정보가 성공적으로 수정되었습니다.");
			} else {
				resultMap.put("success", false);
				resultMap.put("message", resultMap.getOrDefault("message", "일정 및 알림 정보 수정에 실패했습니다."));
			}
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("message", "수정 처리 중 오류가 발생했습니다.");
			resultMap.put("error", e.getMessage());
		}
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

//	// 내캘린더 뿌려주
//	@PostMapping("/sch/schSoloList")
//	@ResponseBody
//	public List<SchsVO> getSoloCalendars(HttpSession session) {
//		Integer empNo = (Integer) session.getAttribute("userEmpNo");
//		return schsService.soloCal(empNo);
//	}
	// 공유조회
		@GetMapping("/cal/calAllList")
		@ResponseBody
		public List<SchsVO> getAllCalendars(HttpSession session) {
			// 세션에서 userId 값을 가져옴
			Integer empNo = (Integer) session.getAttribute("userEmpNo");

			// userId를 기준으로 팀 캘린더 목록을 가져옴 (예: schsService에서 userId 사용)
			return schsService.allCal(empNo);
		}
		
		
	// 공유조회
	@GetMapping("/cal/calTeamList")
	@ResponseBody
	public List<SchsVO> getTeamCalendars(HttpSession session) {
		// 세션에서 userId 값을 가져옴
		Integer empNo = (Integer) session.getAttribute("userEmpNo");

		// userId를 기준으로 팀 캘린더 목록을 가져옴 (예: schsService에서 userId 사용)
		return schsService.teamCal(empNo);
	}

	// 등록
	@PostMapping("/cal/calInsert")
	@ResponseBody
	public Map<String, Object> insertCals(@RequestBody SchsVO schsVO) {
		Map<String, Object> result = new HashMap<>();
		try {
			schsService.insertCals(schsVO); // Service를 통해 캘린더 등록
			result.put("success", true);
			result.put("calsVO", schsVO); // 등록된 캘린더 번호 반환
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", "캘린더 등록에 실패했습니다.");
		}
		return result;
	}

	// 캘린더 수정
	@PostMapping("/cal/calUpdate")
	@ResponseBody
	public Map<String, Object> updateCal(@RequestBody SchsVO schsVO) {
		Map<String, Object> resultMap = schsService.updateCals(schsVO);

		if (resultMap.get("result").equals(true)) {
			resultMap.put("message", "캘린더가 성공적으로 수정되었습니다.");
		} else {
			resultMap.put("message", "캘린더 수정에 실패했습니다.");
		}

		return resultMap;
	}

	// 캘린더 휴지통으로 이동
	@PostMapping("/cal/calTrash")
	@ResponseBody
	public Map<String, Object> moveTrash(@RequestBody Map<String, String> params) {
		int calNo = Integer.parseInt(params.get("calNo")); // String을 int로 변환

		int result = schsService.moveTrash(calNo); // int로 처리
		Map<String, Object> response = new HashMap<>();
		if (result > 0) {
			response.put("success", true);
			response.put("message", "캘린더가 휴지통으로 이동되었습니다.");
		} else {
			response.put("success", false);
			response.put("message", "캘린더 이동에 실패했습니다.");
		}
		return response;
	}

	@PostMapping("/cal/calRestore")
	@ResponseBody
	public Map<String, Object> calRestore(@RequestBody Map<String, String> params) {
		int calNo = Integer.parseInt(params.get("calNo"));

		int result = schsService.calRestore(calNo);
		Map<String, Object> response = new HashMap<>();
		if (result > 0) {
			response.put("success", true);
			response.put("message", "캘린더가 복원되었습니다.");
		} else {
			response.put("success", false);
			response.put("message", "캘린더 복원에 실패했습니다.");
		}
		return response;
	}

	// 휴지통에 있는 캘린더 목록 조회
	@GetMapping("/cal/trashList")
	@ResponseBody
	public List<SchsVO> trashList() {
		return schsService.trashList(); // 휴지통에 있는 캘린더 목록 조회
	}

	@PostMapping("/cal/calDel")
	@ResponseBody
	public String permanentlyDelete(@RequestBody Map<String, Integer> params) {
		int calNo = params.get("calNo");
		int result = schsService.permanentlyDel(calNo);
		return result > 0 ? "캘린더가 완전히 삭제되었습니다." : "캘린더 삭제에 실패했습니다.";
	}

	// 부서별 사원 목록 조회 (캘린더 번호 기반)
    @GetMapping("/cal/deptEmp")
    @ResponseBody
    public List<Map<String, Object>> getDeptEmployees(@RequestParam int calNo) {
        return schsService.getDeptEmp(calNo);
    }

}
