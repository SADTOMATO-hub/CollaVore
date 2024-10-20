package com.collavore.app.cals.mapper;

import java.util.List;

import com.collavore.app.cals.service.CalsVO;

public interface CalsMapper {
	// 전체조회
	public List<CalsVO> selectSoloCal(); // 개인 일정 조회

	public List<CalsVO> selectTeamCal(); // 공유 일정 조회
	
	public List<CalsVO> selectProjCal(); // 공유 일정 조회
}