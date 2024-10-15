package com.collavore.app.cals.service;

import java.util.List;

public interface SchsService {
	//전체조회
	public List<SchsVO> schList();
	
	//일정등록
	public int insertSch (SchsVO schsVO);
	
}