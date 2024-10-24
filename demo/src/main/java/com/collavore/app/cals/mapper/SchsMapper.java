package com.collavore.app.cals.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.collavore.app.cals.service.CalsVO;
import com.collavore.app.cals.service.SchsVO;

public interface SchsMapper {

	// 전체 일정 조회
	public List<SchsVO> selectSchsAll();

	// 등록
	public int insertSchsInfo(SchsVO schsVO);
	// type에 따른 cal_no 조회
    public int selectCalType(String type);
	
	
	

	// 단건조회
	public SchsVO selectSchsInfo(SchsVO schsVO);

	// 수정
	public int updateSchsInfo(SchsVO schsVO);

	// 삭제 조건
	public int deleteSchsInfo(int schsNO);
	
	
	//===캘린더관련

	// 공유캘린더 조회
	public List<CalsVO> selectTeamCal(); // 공유 일정 조회

	// 캘린더 등록
	public int insertCalsInfo(CalsVO calsVO);

	// 캘린더 수정
	public int updateCalsInfo(CalsVO calsVO);

	// 캘린더삭제
	// 휴지통 리스트 
	public List<CalsVO> selectToTrash();

	public int updateCalToTrash(int calNo); // 캘린더를 휴지통으로 이동

	public int restoreCalFromTrash(int calNo); // 캘린더 복원 (isDelete를 'h2'로 업데이트)

	public int permanentlyDeleteCal(int calNo); // 캘린더 완전 삭제 (휴지통에 있는 캘린더 삭제)

}