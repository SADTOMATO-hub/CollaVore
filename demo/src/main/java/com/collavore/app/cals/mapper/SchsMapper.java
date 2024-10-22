package com.collavore.app.cals.mapper;

import java.util.List;

import com.collavore.app.cals.service.CalsVO;
import com.collavore.app.cals.service.SchsVO;

public interface SchsMapper {

	// 전체 일정 조회
	public List<SchsVO> selectSchsAll();

	// 등록
	public int insertSchsInfo(SchsVO schsVO);

	// 단건조회
	public SchsVO selectSchsInfo(SchsVO schsVO);

	// 수정
	public int updateSchsInfo(SchsVO schsVO);

	// 삭제 조건
	public int deleteSchsInfo(int schsNO);

	// 전체조회
	public List<CalsVO> selectAllCal();

	public List<CalsVO> selectSoloCal(); // 개인 일정 조회

	public List<CalsVO> selectTeamCal(); // 공유 일정 조회

	public List<CalsVO> selectProjCal(); // 공유 일정 조회

	// 등록
	public int insertCalsInfo(CalsVO calsVO);

	
	//삭제
	
	public int updateCalToTrash(String calNo); // 캘린더를 휴지통으로 이동 (isDelete를 'h1'로 업데이트)
    
   
	public int restoreCalFromTrash(String calNo);  // 캘린더 복원 (isDelete를 'h2'로 업데이트)
    
   
	public int permanentlyDeleteCal(String calNo);  // 캘린더 완전 삭제 (휴지통에 있는 캘린더 삭제)
	 
}