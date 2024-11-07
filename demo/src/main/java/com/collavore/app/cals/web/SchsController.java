package com.collavore.app.cals.web;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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
import com.collavore.app.service.HomeService;
import com.collavore.app.service.HomeVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller // @Controller 대신 @RestController 사용
@RequiredArgsConstructor
public class SchsController {
	private final SchsService schsService;
	private final HomeService homeService;

		// 사이드바
	@ModelAttribute
	public void addAttributes(Model model, HttpSession session) {
		List<HomeVO> employeesInfo = homeService.empList();
		model.addAttribute("employees", employeesInfo);

		String userAdmin = (String) session.getAttribute("userAdmin");
		model.addAttribute("userAdmin", userAdmin);

		@SuppressWarnings("unchecked")
		List<String> menuAuth = (List<String>) session.getAttribute("menuAuth");
		model.addAttribute("menuAuth", menuAuth);
		
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
	@PostMapping("/sch/updateTime")
	public ResponseEntity<String> updateEventTime(@RequestBody Map<String, Object> payload) {
	    try {
	        // schNo를 String으로 받고 Integer로 변환
	        Integer schNo = Integer.parseInt(payload.get("schNo").toString());
	        String startDateString = payload.get("startDate").toString();
	        String endDateString = payload.get("endDate").toString();
	        
	        // 날짜 형식을 지정합니다.
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        
	        // String을 Date로 변환 후, Date를 Timestamp로 변환합니다.
	        Timestamp startDate = new Timestamp(sdf.parse(startDateString).getTime());
	        Timestamp endDate = new Timestamp(sdf.parse(endDateString).getTime());
	        
			SchsVO svo = new SchsVO();
			svo.setSchNo(schNo);
			svo.setStartDate(startDate);
			svo.setEndDate(endDate);

	        // 필수 값 확인
	        if (schNo == null || startDate == null || endDate == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("failure: Missing required fields (schNo, startDate, or endDate)");
	        }

	        // 업데이트 요청
	        int updatedRows = schsService.updateEventTime(svo);

	        if (updatedRows > 0) {
	            return ResponseEntity.ok("success");
	        } else {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failure: No rows were updated");
	        }

	    } catch (Exception e) {
	        System.err.println("Error in updateEventTime: " + e.getMessage());
	        e.printStackTrace();

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error: An error occurred while updating the event time.");
	    }
	}

		

	// 단건 조회 API
	@GetMapping("/sch/schInfo")
	public ResponseEntity<Map<String, Object>> getEventById(@RequestParam Integer schNo, HttpSession session) {
		Integer empNo = (Integer) session.getAttribute("userEmpNo");
		Map<String, Object> result = new HashMap<>();

		if (empNo == null) {
			result.put("success", false);
			result.put("message", "로그인이 필요합니다.");
			return ResponseEntity.ok(result);
		}

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
//	@PostMapping("/sch/schDelete")
//	@ResponseBody
//	public Map<String, Object> deleteSchs(@RequestBody Map<String, Integer> request) {
//		Map<String, Object> result = new HashMap<>();
//		try {
//			schsService.deleteSchs(request.get("schNo"));
//			result.put("success", true);
//		} catch (Exception e) {
//			result.put("success", false);
//		}
//		return result;
//	}
	// 삭제// 삭제// 삭제// 삭제// 삭제// 삭제// 삭제// 삭제// 삭제// 삭제// 삭제// 삭제// 삭제// 삭제// 삭제//
	// 삭제// 삭제
	@PostMapping("/sch/schDelete")
	@ResponseBody
	public Map<String, Object> deleteSchedule(@RequestBody Map<String, Integer> params) {
		int schNo = params.get("schNo");
		int result = schsService.deleteSchedule(schNo);

		Map<String, Object> response = new HashMap<>();
		response.put("success", result > 0);
		response.put("message", result > 0 ? "일정이 삭제되었습니다." : "일정 삭제에 실패했습니다.");

		System.out.println("213213211132132133333333333333333333333333333333333333333333333");
		System.out.println(result);
		System.out.println("213213211132132133333333333333333333333333333333333333333333333");

		return response;
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

	@PostMapping("/cal/calInsert")
	@ResponseBody
	public Map<String, Object> insertCals(@RequestBody Map<String, Object> requestData, HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		try {
			// 세션에서 현재 사용자의 empNo 가져오기
			Integer empNo = (Integer) session.getAttribute("userEmpNo");

			// 캘린더 정보 설정
			SchsVO schsVO = new SchsVO();
			schsVO.setName((String) requestData.get("name"));
			schsVO.setColor((String) requestData.get("color"));
			schsVO.setCalType((String) requestData.get("type"));
			schsVO.setIsDelete((String) requestData.get("isDelete"));

			// 캘린더 정보 저장
			int calNo = schsService.insertCals(schsVO);

			// 참여자 목록 추출 및 본인 empNo 추가
			List<Integer> members = ((List<?>) requestData.get("members")).stream()
					.map(e -> Integer.parseInt(e.toString())).collect(Collectors.toList());
			// 현재 사용자가 이미 포함되어 있는지 확인한 후, 없는 경우에만 추가
			if (!members.contains(empNo)) {
				members.add(empNo);
			}

			// 참여자 정보 저장
			if (calNo > 0 && !members.isEmpty()) {
				schsService.insertCalShares(calNo, members);
			}
			SchsVO calInfo = schsService.selectCalInfo(calNo);

			result.put("success", true);
			result.put("calNo", calNo);
			result.put("calInfo", calInfo);
		} catch (Exception e) {
			result.put("success", false);
			result.put("message", "캘린더 등록에 실패했습니다.");
		}
		return result;
	}

	// 캘린더 수정
//	@PostMapping("/cal/calUpdate")
//	@ResponseBody
//	public Map<String, Object> updateCal(@RequestBody SchsVO schsVO) {
//		Map<String, Object> resultMap = schsService.updateCals(schsVO);
//
//		if (resultMap.get("result").equals(true)) {
//			resultMap.put("message", "캘린더가 성공적으로 수정되었습니다.");
//		} else {
//			resultMap.put("message", "캘린더 수정에 실패했습니다.");
//		}
//
//		return resultMap;
//	}
	// 캘린더 수정
	@PostMapping("/cal/calUpdate")
	@ResponseBody
	public Map<String, Object> updateCalendarWithParticipants(@RequestBody Map<String, Object> requestData,
			HttpSession session) {
		Map<String, Object> response = new HashMap<>();

		try {
			// calNo가 String으로 전달되었다면 Integer로 변환
			int calNo;
			if (requestData.get("calNo") instanceof String) {
				calNo = Integer.parseInt((String) requestData.get("calNo"));
			} else {
				calNo = (int) requestData.get("calNo");
			}

			String name = (String) requestData.getOrDefault("name", "");
			String color = (String) requestData.getOrDefault("color", "");

			// color 값이 제대로 설정되었는지 확인
			System.out.println("=========================================");
			System.out.println("Received color for update: " + color);
			System.out.println("=========================================");

			Integer empNo = (Integer) session.getAttribute("userEmpNo");
			if (empNo == null) {
				throw new IllegalArgumentException("User not logged in or empNo missing from session.");
			}
			System.out.println("Current user empNo: " + empNo);

			List<?> membersRaw = (List<?>) requestData.get("members");
			if (membersRaw == null || membersRaw.isEmpty()) {
				throw new IllegalArgumentException("Members list is missing or empty in request data.");
			}

			List<Integer> members = membersRaw.stream().map(e -> {
				if (e instanceof String) {
					return Integer.parseInt((String) e);
				} else if (e instanceof Integer) {
					return (Integer) e;
				} else {
					throw new IllegalArgumentException("Unexpected data type in members list: " + e.getClass());
				}
			}).collect(Collectors.toList());

			System.out.println("Processed members list: " + members);

			if (!members.contains(empNo)) {
				members.add(empNo);
			}

			int result = schsService.updateCalendarWithParticipants(calNo, name, color, members);
			response.put("result", result > 0);
			response.put("message", result > 0 ? "Calendar update successful" : "No updates were made");

		} catch (Exception e) {
			e.printStackTrace();
			response.put("result", false);
			response.put("message", "Error updating calendar: " + e.getMessage());
		}

		return response;
	}

	@GetMapping("/cal/getCalDeptEmpInfo")
	@ResponseBody
	public List<Map<String, Object>> getCalDeptEmpInfo(@RequestParam int calNo) {
		// SchsService에서 지정된 calNo에 해당하는 참여자 목록을 가져옵니다.
		return schsService.getCalInfo(calNo);
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

	// 휴지통에있는 캘린더 복원
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
	
	@GetMapping("/cal/projList")
	@ResponseBody
	public List<SchsVO> projList(HttpSession session) {
		Integer empNo = (Integer) session.getAttribute("userEmpNo");
		return schsService.projList(empNo); // 휴지통에 있는 캘린더 목록 조회
	}
	
	

//	// 완전삭제1
//	@PostMapping("/cal/calDel")
//	@ResponseBody
//	public String permanentlyDelete(@RequestBody Map<String, Integer> params) {
//		int calNo = params.get("calNo");
//		int result = schsService.permanentlyDel(calNo);
//		return result > 0 ? "캘린더가 완전히 삭제되었습니다." : "캘린더 삭제에 실패했습니다.";
//	}
	// 완전삭제 요청2
	@PostMapping("/cal/calDel")
	@ResponseBody
	public String permanentlyDel(@RequestBody Map<String, Integer> params) {
		int calNo = params.get("calNo");
		int result = schsService.permanentlyDel(calNo); // 서비스 메서드 이름과 일치시킴
		return result > 0 ? "캘린더가 완전히 삭제되었습니다." : "캘린더 삭제에 실패했습니다.";
	}

	@GetMapping("/cal/deptWithEmp")
	@ResponseBody
	public List<Map<String, Object>> getDeptWithEmployees() {
		return schsService.getDeptWithEmp();
	}

//	@GetMapping("/cal/deptList")
//	@ResponseBody
//	public List<SchsVO> getDeptList() {
//	    return schsService.getDeptList();
//	}
//
//	// 부서별 사원 목록 조회 (캘린더 번호 기반)
//    @GetMapping("/cal/deptEmp")
//    @ResponseBody
//    public List<Map<String, Object>> getDeptEmployees(@RequestParam int deptNo) {
//        return schsService.getDeptEmp(deptNo);
//    }

}
