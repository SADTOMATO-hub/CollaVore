package com.collavore.app.api.flutter;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public int chkUserProcess(@RequestBody FlutterVO flutterVO) {
		int result = flutterService.userChk(flutterVO);
		return result > 0 ? result : -1;
	}

	// 비밀번호찾기 - 비밀번호변경
	@PostMapping("/pwdModify")
	public int pwdModifyProcess(@RequestBody FlutterVO flutterVO) {
		int result = flutterService.pwdModify(flutterVO);
		return result > 0 ? result : -1;
	}
	// 일정목록조회
	// 일정등록
	// 일정상세
	// 일정수정
	// 프로젝트목록조회
	// 프로젝트업무목록조회
	// 프로젝트상세업무목록조회
	// 프로젝트상세업무상세보기
	// 프로젝트상세업무상세보기-댓글조회
	// 프로젝트상세업무상세보기-댓글등록
	// 프로젝트상세업무상세보기-댓글삭제
	// 전자결재문서목록조회
	// 전자결재문서상세보기
	// 전자결재문서승인,반려처리
	// 회원정보수정

}
