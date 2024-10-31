package com.collavore.app.cals.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collavore.app.cals.mapper.SchsMapper;
import com.collavore.app.cals.service.CalsVO;
import com.collavore.app.cals.service.SchsService;
import com.collavore.app.cals.service.SchsVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SchsServiceImpl implements SchsService {
	private final SchsMapper schsMapper;

	// 조회
	@Override
	public List<SchsVO> SchsList(int empNo) {
		return schsMapper.selectSchsAll(empNo);
	}

	// 단건조회
	@Override
	public SchsVO SchsInfo(SchsVO schsVO) {
		return schsMapper.selectSchsInfo(schsVO);
	}

	// 등록
	@Override
	public int insertSchs(SchsVO schsVO) {
		int result = schsMapper.insertSchsInfo(schsVO);
		System.out.println("Type: " + schsVO.getCalType());

		return result == 1 ? schsVO.getSchNo() : -1;

	}

	// 등록
	@Override
	public int insertAlarm(SchsVO schsVO) {
		System.out.println("insertAlarm isAlarm value: " + schsVO.getIsAlarm()); // 여기서 확인
		return schsMapper.alarmInsert(schsVO);
	}

	@Override
	public int getCalType(String type) {
		return schsMapper.selectCalType(type);
	}

	// 기존 일정 수정 메서드
	@Override
	public Map<String, Object> updateSchs(SchsVO schsVO) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// 1. 일정 정보 업데이트
			int updatedRows = schsMapper.updateSchsInfo(schsVO);
			if (updatedRows > 0) {
				resultMap.put("result", true);
				resultMap.put("message", "일정이 성공적으로 수정되었습니다.");

				// 2. 알림 정보 수정
				int alarmResult = updateAlarm(schsVO);
				if (alarmResult > 0) {
					resultMap.put("alarmUpdate", "알림 정보가 성공적으로 수정되었습니다.");
				} else {
					resultMap.put("alarmUpdate", "알림 정보 수정에 실패하였습니다.");
				}
			} else {
				resultMap.put("result", false);
				resultMap.put("message", "수정할 일정이 없습니다.");
			}
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("message", "수정 중 오류가 발생했습니다.");
			resultMap.put("error", e.getMessage());
		}
		return resultMap;
	}

	@Override
	public int updateAlarm(SchsVO schsVO) {
		int affectedRows = 0;

		if ("f1".equals(schsVO.getIsAlarm())) {
			// 알림 정보가 모두 유효한 경우에만 삽입
			if (schsVO.getAlarmType() != null || schsVO.getAlarmYoil() != null || schsVO.getAlarmDay() != null
					|| schsVO.getAlarmTime() != null) {

				// 기존 알림 정보 삭제
				schsMapper.deleteAlarmInfo(schsVO.getSchNo());

				// 새로운 알림 정보 삽입
				affectedRows = schsMapper.insertAlarmInfo(schsVO);
			}
		} else if ("f2".equals(schsVO.getIsAlarm())) {
			// 알림 설정이 꺼져 있는 경우 알림 정보 삭제
			affectedRows = schsMapper.deleteAlarmInfo(schsVO.getSchNo());
		}

		return affectedRows;
	}

	// 알림 정보 추가
	@Override
	public int insertAlarmInfo(SchsVO schsVO) {
		return schsMapper.insertAlarmInfo(schsVO);
	}

	// 알림 정보 삭제
	@Override
	public int deleteAlarmInfo(int schNo) {
		return schsMapper.deleteAlarmInfo(schNo);
	}

	// 삭제
	@Override
	public int deleteSchs(int schsNO) {
		return schsMapper.deleteSchsInfo(schsNO);
	}

	// =====================캘린더 사이드바=====================
	// 일정생성창 캘린더 전체조회
	@Override
	public List<SchsVO> allCal(int empNo) {
		return schsMapper.selectAllCal(empNo);
	}

	// 공유캘린더 조회 (is_delete가 'h2'인 항목만)
	@Override
	public List<SchsVO> teamCal(int empNo) {
		return schsMapper.selectTeamCal(empNo);
	}

	// 프로젝트 캘린더

	// 첫 번째 인서트: 캘린더 등록
    @Override
    public int insertCals(SchsVO schsVO) {
        schsMapper.insertCalsInfo(schsVO); // 캘린더 정보 저장
        return schsVO.getCalNo(); // 생성된 캘린더 번호 반환
    }

 // 두 번째 인서트: 참여자 정보 저장
    @Override
    public int insertCalShares(int calNo, List<Integer> empNos) {
        int count = 0;
        for (Integer empNo : empNos) {
            Map<String, Object> params = new HashMap<>();
            params.put("calNo", calNo);
            params.put("empNo", empNo);

            // 디버그: 각 삽입 시도를 로깅
            System.out.println("Inserting calNo: " + calNo + ", empNo: " + empNo);

            count += schsMapper.insertCalShares(params); // 매퍼 호출
        }
        System.out.println("Total inserted rows: " + count);
        return count;
    }


	// 캘린더 수정
//	@Override
//	public Map<String, Object> updateCals(SchsVO schsVO) {
//		Map<String, Object> resultMap = new HashMap<>();
//		try {
//			int updatedRows = schsMapper.updateCalsInfo(schsVO); // 매퍼 호출
//
//			if (updatedRows > 0) {
//				resultMap.put("result", true);
//			} else {
//				resultMap.put("result", false);
//			}
//		} catch (Exception e) {
//			resultMap.put("result", false);
//		}
//		return resultMap;
//	}
    @Override
    @Transactional
    public int updateCalendarWithParticipants(int calNo, String name, String color, List<Integer> empNos) {
        // 캘린더 기본 정보 업데이트
        int updateCount = schsMapper.updateCalendarDetails(calNo, name, color);

        // 기존 참여자 삭제
        schsMapper.deleteParticipantsByCalNo(calNo);

        // 새 참여자 추가
        int addCount = 0;
        for (Integer empNo : empNos) {
            Map<String, Object> params = new HashMap<>();
            params.put("calNo", calNo);
            params.put("empNo", empNo);
            addCount += schsMapper.addParticipant(params);
        }

        return updateCount + addCount; // 총 성공한 업데이트 및 추가 작업의 수 반환
    }
	//캘린더 수정할때 저장된 기본값 불러오기 부서 사원 참여자 
	@Override
	public List<Map<String, Object>> getCalInfo(int calNo) {
	    return schsMapper.getCalInfo(calNo);
	}
	
	
	
	
	
	
	
	

	// 공유캘린더 조회 (is_delete가 'h2'인 항목만)
	@Override
	public List<SchsVO> trashList() {
		return schsMapper.selectToTrash();
	}

	// 캘린더를 휴지통으로 이동 (isDelete = 'h1')
	@Override
	public int moveTrash(int calNo) {
		int result = schsMapper.updateCalToTrash(calNo);
		return result; // 성공한 row 개수를 그대로 반환
	}

	// 캘린더 복원 (isDelete = 'h2'로 업데이트)
	@Override
	public int calRestore(int calNo) {
		return schsMapper.restoreCalFromTrash(calNo);
	}

	// 캘린더 완전 삭제
	@Override
	public int permanentlyDel(int calNo) {
		return schsMapper.permanentlyDeleteCal(calNo);
	}

	@Override
	public List<Map<String, Object>> getDeptWithEmp() {
		return schsMapper.selectDeptWithEmp();
	}
//	@Override
//	public List<SchsVO> getDeptList() {
//	    return schsMapper.selectDeptList();
//	}
//	// 서비스
//	@Override
//	public List<Map<String, Object>> getDeptEmp(int deptNo) {
//	    return schsMapper.selectDeptEmp(deptNo);
//	}
	// =====================END 캘린더 사이드바=====================
	// =====================알림관리========================

	
	// 새로 등록된 사원에 내캘린더 생성
	@Override
	public int addNewMyCal(int empNo) {
	    Map<String, Object> result = new HashMap<>();
	    result.put("result", null);  // 초기값 설정
	    try {
	        // 프로시저 호출
		    schsMapper.insertMyCal(empNo, result);

	        // 결과 값이 null인 경우 기본값 0 반환
	        Integer resultValue = (Integer) result.get("result");
	        return (resultValue != null) ? resultValue : 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return 0;  // 예외 발생 시 기본값 반환
	    }
	}
}
