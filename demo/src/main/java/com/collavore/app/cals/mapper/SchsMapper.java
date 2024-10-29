package com.collavore.app.cals.mapper;

import java.util.List;


import com.collavore.app.cals.service.SchsVO;

public interface SchsMapper {

	// 전체 일정 조회
	public List<SchsVO> selectSchsAll(int empNo);

	// 등록
	public int insertSchsInfo(SchsVO schsVO);
	// 등록
		public int alarmInsert(SchsVO schsVO);
	// type에 따른 cal_no 조회
    public int selectCalType(String type);
	
	
	

	// 단건조회
	public SchsVO selectSchsInfo(SchsVO schsVO);

	// 수정
	public int updateSchsInfo(SchsVO schsVO);
	// 수정 알림등록
	public int updateAlarmInfo(SchsVO schsVO);
	 // 알림 정보 삽입 (알림이 없는 경우 새로 삽입)
    public int insertAlarmInfo(SchsVO schsVO);
    // 알림 정보 삭제 (알림 설정이 비활성화된 경우)
    public int deleteAlarmInfo(int schNo);
    // 해당 sch_no의 알림 존재 여부 확인
    public int checkAlarm(int schNo);
	
	
	
	
	
	

	// 삭제 조건
	public int deleteSchsInfo(int schsNO);
	
	
	//===캘린더관련

	// 공유캘린더 조회
	public List<SchsVO> selectTeamCal(int empNo); // 공유 일정 조회

	// 캘린더 등록
	public int insertCalsInfo(SchsVO schsVO);

	// 캘린더 수정
	public int updateCalsInfo(SchsVO schsVO);

	// 캘린더삭제
	// 휴지통 리스트 
	public List<SchsVO> selectToTrash();

	public int updateCalToTrash(int calNo); // 캘린더를 휴지통으로 이동

	public int restoreCalFromTrash(int calNo); // 캘린더 복원 (isDelete를 'h2'로 업데이트)

	public int permanentlyDeleteCal(int calNo); // 캘린더 완전 삭제 (휴지통에 있는 캘린더 삭제)
	
	
	

}