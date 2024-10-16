package com.collavore.app.cals.mapper;

import java.util.List;

import com.collavore.app.cals.service.SchsVO;

public interface SchsMapper {
	//전체조회
	public List<SchsVO> selectSchsAll();
	
	// 일정 추가
    public int insertSch(SchsVO schsVO);
}