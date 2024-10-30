package com.collavore.app.board.service;

import java.util.List;
import java.util.Map;


public interface BodsService {
	// 게시글 전체 건수 확인하기(페이징을 위함)
	public int totalListCnt(BodsVO bodsVO);
	
	// 게시글 전체 목록 전체조회
	public List<BodsVO> bodsList(BodsVO bodsVO);

	// 게시글상세보기(단건조회)
	public BodsVO bodsInfo(BodsVO bodsVO);

	// 게시글등록(등록)
	public int insertBods(BodsVO bodsVO);

	// 게시글수정(수정)
	public Map<String, Object> updateBods(BodsVO bodVO);

	// 게시글 삭제
	public int deleteBods(int bodVO);

	// 댓글 등록
	public int insertBodsComts(BodsComtsVO bodsComtsVO);
	
	// 댓글 전체조회
	public List<BodsComtsVO> bodsComtsList(int postNo);
	
	// 댓글 삭제
	public int deleteBodsComts(int bodsComtsVO);
	
	// 댓글 수정
	public Map<String, Object> updateBodsComts(BodsComtsVO bodsComtsVO);
	
	// 댓글 상세조회
	public BodsComtsVO bodsComtsInfo(BodsComtsVO bodsComtsVO);

	
	// 게시판 전체 조회
	public List<BodsCfigVO> bodsListAll(BodsCfigVO bodsCfigVO);
	
	// 게시판 등록
	public int insertBodsCfig(BodsCfigVO bodsCfigVO);
	
	// 게시판 수정
	public Map<String, Object> updateBodsCfig(BodsCfigVO bodsCfigVO);
	
	//게시판 상세조회
	public BodsCfigVO bodsCfigInfo(BodsCfigVO bodsCfigVO);
	
	// 게시판 삭제
	public int deleteBodsCfig(int bodsCfigVO);
	
	// 게시판 상세조회
	// public 

	// 전체조회
	// public List<BodsVO> bodsAll();
	
	// 게시판이름 조회
	public String boardNameSearch(int boardNo);

}
