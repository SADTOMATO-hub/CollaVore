package com.collavore.app.cals.service;

import java.util.List;
import java.util.Map;

public interface SchsService {
	// 조회
	public List<SchsVO> SchsList(int empNo);

	// 단건조회
	public SchsVO SchsInfo(SchsVO schsVO);

	// 등록
	public int insertSchs(SchsVO schsVO);

	// 알림관리 ================================
	// 등록
	public int insertAlarm(SchsVO schsVO);

	// 캘린더 타입에 따른 cal_no 조회
	public int getCalType(String type);

	// 수정
	public Map<String, Object> updateSchs(SchsVO schsVO);

	// 알림관리 ================================
	// 수정 알림설정
	public int updateAlarm(SchsVO schNo);

	// 알림 정보 추가
	public int insertAlarmInfo(SchsVO schsVO);

	// 알림 정보 삭제
	public int deleteAlarmInfo(int schNo);
	// end 알림관리 ================================

	// 삭제
	public int deleteSchs(int schsNO);

// 캘린더 =====================================
	//일정생성시 캘린더 전체조회
	public List<SchsVO> allCal(int empNo); // 공유 일정 조회
	

	// 공유 캘린더 조회
	public List<SchsVO> teamCal(int empNo); // 공유 일정 조회
	
	

	// 캘린더 등록
	public int insertCals(SchsVO schsVO);
	// 공유 캘린더 참여자 등록
	public int insertCalShares(int calNo, List<Integer> members); // 참여자 정보를 저장하고 성공한 레코드 수를 반환
	
	
	
	
	// 캘린더 수정
	public Map<String, Object> updateCals(SchsVO schsVO);

	// 휴지통 리스트
	// 조회
	public List<SchsVO> trashList();

	// 캘린더 삭제
	public int moveTrash(int schsVO); // 캘린더 휴지통으로 이동

	public int calRestore(int schsVO); // 캘린더 복원

	public int permanentlyDel(int schsVO); // 캘린더 완전 삭제
	
	
	
	
	List<Map<String, Object>> getDeptWithEmp();



//	public List<SchsVO> getDeptList();
//	//캘린더 추가 수정시 부서 사원번호 리스트
//	public  List<Map<String, Object>> getDeptEmp(int deptNo);
	
	// 새로 등록된 사원에 내캘린더 생성
	public int addNewMyCal(int empNo);

}