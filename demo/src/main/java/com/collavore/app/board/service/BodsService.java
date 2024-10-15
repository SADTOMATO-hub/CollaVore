package com.collavore.app.board.service;

import java.util.List;
import java.util.Map;

public interface BodsService {
	// 게시글 전체 목록 전체조회
	public List<BodsVO> bodsList();

	// 게시글상세보기(단건조회)
	public BodsVO bodsInfo(BodsVO bodsVO);

	// 게시글등록(등록)
	public int insertBods(BodsVO bodsVO);

	// 게시글수정(수정)
	public Map<String, Object> updateBods(BodsVO bodVO);

	// 게시글 삭제
	public int deleteBods(int bodVO);
	
	// 게시판 생성
	// public int insertBods
	
	// 게시판 수정
	// public 
	
	// 게시판 상세조회
	// public 

	// 전체조회
	// public List<BodsVO> bodsAll();

}
