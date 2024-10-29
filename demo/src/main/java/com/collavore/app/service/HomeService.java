package com.collavore.app.service;

import java.util.List;

public interface HomeService {
	// 게시판조회
	public List<HomeVO> selBoardList();
	
	// 공지사항게시글조회
	public List<HomeVO> selNoticeList();
	
	// 내가 기안한 전자결재
	public List<HomeVO> selAppList(int empNo);
}
