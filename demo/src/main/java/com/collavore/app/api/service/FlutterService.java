package com.collavore.app.api.service;

import java.util.List;

public interface FlutterService {
	// 로그인
	public FlutterVO loginChk(FlutterVO flutterVO);
	
	// 아이디찾기
	public String findId(FlutterVO flutterVO);
	
	// 비밀번호찾기-계정유무확인
	public int userChk(FlutterVO flutterVO);
	
	// 비밀번호찾기-비밀번호변경
	public int pwdModify(FlutterVO flutterVO);
	
	// 일정목록조회
	public List<FlutterSchsVO> schsAll(int empNo);
	
	// 일정등록
	public int schsAdd(FlutterVO flutterVO);
	
	// 일정상세
	public FlutterSchsVO schsInfo(int schsNo);
	
	// 일정수정
	public int schsModify(FlutterVO flutterVO);
	
	// 프로젝트목록조회
	public List<FlutterVO> projAll(FlutterVO flutterVO);
	
	// 프로젝트업무목록조회
	public List<FlutterVO> projWorkAll(FlutterVO flutterVO);
	
	// 프로젝트상세업무목록조회
	public List<FlutterVO> projWorkDetailAll(FlutterVO flutterVO);
	
	// 프로젝트상세업무상세보기
	public FlutterVO projWorkDetailInfo(FlutterVO flutterVO);
	
	// 프로젝트상세업무상세보기-댓글조회
	public List<FlutterVO> projCommentAll(FlutterVO flutterVO);
	
	// 프로젝트상세업무상세보기-댓글등록
	public int projCommentAdd(FlutterVO flutterVO);
	
	// 프로젝트상세업무상세보기-댓글삭제
	public int projCommentRemove(FlutterVO flutterVO);
	
	// 전자결재문서목록조회
	public List<FlutterVO> apprAll(FlutterVO flutterVO);
	
	// 전자결재문서상세보기
	public FlutterVO apprInfo(FlutterVO flutterVO);
	
	// 전자결재문서승인,반려처리
	public int apprProc(FlutterVO flutterVO);
	
	// 회원정보수정
	public FlutterVO memberModify(FlutterVO flutterVO);

}
