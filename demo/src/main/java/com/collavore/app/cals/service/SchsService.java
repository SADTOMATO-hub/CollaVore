package com.collavore.app.cals.service;

import java.util.List;
import java.util.Map;

public interface SchsService {
	// 조회
	public List<SchsVO> SchsList();

	// 단건조회
	public SchsVO SchsInfo(SchsVO schsVO);

	// 등록
	public int insertSchs(SchsVO schsVO);
	// 캘린더 타입에 따른 cal_no 조회
	public int getCalType(String type);
	

	// 수정
	public Map<String, Object> updateSchs(SchsVO schsVO);

	// 삭제
	public int deleteSchs(int schsNO);

	// 전체조회
	// 공유 캘린더 조회
    List<CalsVO> teamCal(); // 공유 일정 조회

	// 캘린더 등록
	public int insertCals(CalsVO calsVO);
	
	// 캘린더 수정
	public Map<String, Object> updateCals(CalsVO calsVO);

	
	//휴지통 리스트
	// 조회
	public List<CalsVO>  trashList();
	
	// 캘린더 삭제
	public int moveTrash(int calNo); // 캘린더 휴지통으로 이동

	public int calRestore(int calNo); // 캘린더 복원

	public int permanentlyDel(int calNo); // 캘린더 완전 삭제

}