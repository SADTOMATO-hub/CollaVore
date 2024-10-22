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

	// 수정
	public Map<String, Object> updateSchs(SchsVO schsVO);

	// 삭제
	public int deleteSchs(int schsNO);

	// 전체조회
	public List<CalsVO> allCal();

	public List<CalsVO> soloCal(); // 개인 일정 조회

	public List<CalsVO> teamCal(); // 공유 일정 조회

	public List<CalsVO> projCal(); // 프로젝트 일정 조회

	// 캘린더 등록
	public int insertCals(CalsVO calsVO);

	// 캘린더 삭제
	public String moveTrash(String calNo); // 캘린더 휴지통으로 이동

	public String calRestore(String calNo); // 캘린더 복원

	public String permanentlyDel(String calNo); // 캘린더 완전 삭제

}