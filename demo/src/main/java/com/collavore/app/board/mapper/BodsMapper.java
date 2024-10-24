package com.collavore.app.board.mapper;

import java.util.List;

import com.collavore.app.board.service.BodsComtsVO;
import com.collavore.app.board.service.BodsVO;

public interface BodsMapper {
	// 게시글 전체 수 조회
	public int totalBoardCnt(BodsVO bodsVO);
	
	// 게시글 전체 목록 전체조회
	public List<BodsVO> selectBoardAll(BodsVO bodsVO);

	// 게시글상세보기(단건조회)
	public BodsVO selectBodsInfo(BodsVO bodsVO);

	// 게시글등록(등록)
	public int insertBodsInfo(BodsVO bodsVO);

	// 게시글수정(수정)
	public int updateBodsInfo(BodsVO bodsVO);

	// 게시글삭제
	public int deleteBodsInfo(int postNO);

	// 댓글 등록
	public int insertBodsComtsInfo(BodsComtsVO bodsComtsVO);
	
	// 댓글 조회(전체 조회)
	public List<BodsComtsVO>selectBodsComtsAll(int postNo);
	
	// 댓글 삭제(단건 삭제)
	public int delectBodsComtsInfo(int cmtNo);
	
	// 댓글 수정
	public int updateBodsComtsInfo(BodsComtsVO bodsComtsVO);  
	
	// 게시판 생성(등록)
	// public int insertBodsInfo();

	/*
	 * // 게시판 수정 public int UpdateBodsInfo();
	 * 
	 * // 게시판 삭제 public int delectBodsInfo(int post_no);
	 * 
	 * // 게시판 전체 조회 public List<BodsVO> getlist();
	 */

}
