package com.collavore.app.cals.mapper;

import java.util.List;

import com.collavore.app.cals.service.SchsVO;

public interface SchsMapper {
    List<SchsVO> selectSchsAll(); // 전체 일정 조회
    
	//등록 :
	public int insertSchsInfo(SchsVO schsVO);
}