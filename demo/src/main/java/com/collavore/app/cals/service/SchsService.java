package com.collavore.app.cals.service;

import java.util.List;


public interface SchsService {
	//조회
	List<SchsVO> SchsList();

	// 단건조회
	// 등록
	public int insertSchs(SchsVO SchsVO);
	// 수정
	// 삭제
}