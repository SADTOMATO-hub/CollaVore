package com.collavore.app.mapper;

import java.util.List;

import com.collavore.app.service.HomeVO;

public interface HomeMapper {
	//게시판조회
	public List<HomeVO> selectBoardList();
	
	// 공지사항게시글조회
	public List<HomeVO> selectNoticeList();
	
	// 내가 기안한 전자결재
	public List<HomeVO> selectAppList(int empNo);
}
