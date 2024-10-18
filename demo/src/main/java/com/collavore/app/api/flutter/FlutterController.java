package com.collavore.app.api.flutter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.api.service.FlutterService;
import com.collavore.app.api.service.FlutterVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FlutterController {

	private final FlutterService flutterService;
	
	// 로그인
	@PostMapping("api/login")
	@ResponseBody
	public FlutterVO loginProcess2(@RequestBody FlutterVO flutterVO, Model model) {
		FlutterVO result = flutterService.loginChk(flutterVO);
		return result != null ? result : new FlutterVO();
	}
	
	// 아이디찾기
	// 비밀번호찾기
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
