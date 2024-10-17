package com.collavore.app.cals.service;

import java.util.List;
import java.util.Map;

public interface SchsService {
	// 조회
	List<SchsVO> SchsList();

	// 단건조회
	public SchsVO SchsInfo(SchsVO schsVO);

	// 등록
	public int insertSchs(SchsVO schsVO);

	// 수정
	public Map<String, Object> updateShcs(SchsVO schsVO);

	// 삭제
	public int deleteSchs(int schsNO);
}