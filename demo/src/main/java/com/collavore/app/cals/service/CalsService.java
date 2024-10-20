package com.collavore.app.cals.service;

import java.util.List;

public interface CalsService {
	//조회
	// 전체조회
		public List<CalsVO> soloCal(); // 개인 일정 조회

		public List<CalsVO> teamCal(); // 공유 일정 조회
		
		public List<CalsVO> projCal(); // 프로젝트 일정 조회
	//상세조회
	
	
	//캘린더생성
	
	
	//캘린더삭제
	
	
}