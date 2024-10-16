package com.collavore.app.board.mapper;

import java.util.List;

import com.collavore.app.board.service.BodsVO;

public interface BodsComtsMapper {
	// 댓글 등록
	public List<BodsVO> selectBoardAll();

	/* 댓글 조회
	public BodsVO selectBodsInfo(BodsVO bodsVO);

	// 댓글 수정
	public int insertBodsInfo(BodsVO bodsVO);

	// 댓글 삭제
	public int insertBodsInfo(BodsVO bodsVO);*/
}
