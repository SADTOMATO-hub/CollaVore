package com.collavore.app.api.flutter;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.collavore.app.api.service.FlutterProjVO;
import com.collavore.app.api.service.FlutterSchsVO;
import com.collavore.app.api.service.FlutterService;
import com.collavore.app.api.service.FlutterVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class FlutterController {

	private final FlutterService flutterService;

	// 로그인
	@PostMapping("/login")
	public FlutterVO loginProcess(@RequestBody FlutterVO flutterVO) {
		FlutterVO result = flutterService.loginChk(flutterVO);
		return result != null ? result : new FlutterVO();
	}

	// 아이디찾기
	@PostMapping("/findId")
	public String findIdProcess(@RequestBody FlutterVO flutterVO) {
		String result = flutterService.findId(flutterVO);
		return result != null ? result : "";
	}

	// 비밀번호찾기 - 계정유무확인
	@PostMapping("/chkUser")
	public String chkUserProcess(@RequestBody FlutterVO flutterVO) {
		int result = flutterService.userChk(flutterVO);
		return result > 0 ? "Success" : "Error";
	}

	// 비밀번호찾기 - 비밀번호변경
	@PostMapping("/pwdModify")
	public String pwdModifyProcess(@RequestBody FlutterVO flutterVO) {
		int result = flutterService.pwdModify(flutterVO);
		return result > 0 ? "Success" : "Error";
	}

	// 일정목록조회
	@GetMapping("/schsSelectAll")
	public List<FlutterSchsVO> selSchsAllList(@RequestParam int empNo) {
		List<FlutterSchsVO> mySchs = flutterService.schsAll(empNo);
		return mySchs;
	}

	// 일정등록
	// 일정상세
	@GetMapping("/schsInfo")
	public FlutterSchsVO schsInfo(@RequestParam int schNo) {
		FlutterSchsVO schsInfo = flutterService.schsInfo(schNo);
		return schsInfo;
	}
	
	// 일정수정
	
	// 프로젝트목록조회
	@GetMapping("/projSelectAll")
	public List<FlutterProjVO> selProjAllList(@RequestParam int empNo){
		List<FlutterProjVO> myProjs = flutterService.projAll(empNo);
		return myProjs;
	}
	
	// 프로젝트업무목록조회
	@GetMapping("/projWorkSelectAll")
	public List<FlutterProjVO> selProjWorkAllList(@RequestParam int projNo, @RequestParam int empNo){
		List<FlutterProjVO> myProjWorks = flutterService.projWorkAll(projNo, empNo);
		return myProjWorks;
	}
	
	// 프로젝트상세업무목록조회
	@GetMapping("/projWorkDetailSelectAll")
	public List<FlutterProjVO> selProjWrokDetailAllList(@RequestParam int pwNo, @RequestParam int empNo){
		List<FlutterProjVO> myProjDetailWorks = flutterService.projWorkDetailAll(pwNo, empNo);
		return myProjDetailWorks;
	}
	// 프로젝트상세업무상세보기
	// 프로젝트상세업무상세보기-댓글조회
	// 프로젝트상세업무상세보기-댓글등록
	// 프로젝트상세업무상세보기-댓글삭제
	// 전자결재문서목록조회
	// 전자결재문서상세보기
	// 전자결재문서승인,반려처리
	// 회원정보수정
}
