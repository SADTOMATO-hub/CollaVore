package com.collavore.app.cals.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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
	    System.out.println("Type: " + schsVO.getType());

	    return result == 1 ? schsVO.getSchNo() : -1;

	}
	
	 @Override
	    public int getCalType(String type) {
	        return schsMapper.selectCalType(type);
	    }
	
	
	

	// 수정
	@Override
	public Map<String, Object> updateSchs(SchsVO schsVO) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			int updatedRows = schsMapper.updateSchsInfo(schsVO);

			if (updatedRows > 0) {
				resultMap.put("result", true);
				resultMap.put("message", "일정이 성공적으로 수정되었습니다.");
			} else {

				resultMap.put("result", false);
				resultMap.put("message", "수정할 일정이 없습니다.");
			}
		} catch (Exception e) {
			resultMap.put("result", false);
			resultMap.put("message", "수정 중 오류가 발생했습니다.");
		}
		return resultMap;
	}

	// 삭제
	@Override
	public int deleteSchs(int schsNO) {
		return schsMapper.deleteSchsInfo(schsNO);
	}

	// =====================캘린더 사이드바=====================
	// 사이드바 캘린더 전체

	// 개인캘린더

	// 공유캘린더 조회 (is_delete가 'h2'인 항목만)
	@Override
	public List<SchsVO> teamCal(int empNo) {
		return schsMapper.selectTeamCal(empNo);
	}

	// 프로젝트 캘린더

	// 캘린더 등록
	@Override
	public int insertCals(SchsVO schsVO) {
		int result = schsMapper.insertCalsInfo(schsVO);
		return result == 1 ? schsVO.getCalNo() : -1;

	}

	// 캘린더 수정
	@Override
	public Map<String, Object> updateCals(SchsVO schsVO) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			int updatedRows = schsMapper.updateCalsInfo(schsVO); // 매퍼 호출

			if (updatedRows > 0) {
				resultMap.put("result", true);
			} else {
				resultMap.put("result", false);
			}
		} catch (Exception e) {
			resultMap.put("result", false);
		}
		return resultMap;
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

	// =====================END 캘린더 사이드바=====================
	// =====================알림관리========================
	// 등록
		@Override
		public int alaInsert(SchsVO schsVO) {
		    int result = schsMapper.insertSchsInfo(schsVO);
		    System.out.println("Type: " + schsVO.getType());

		    return result == 1 ? schsVO.getSchNo() : -1;

		}

		
	
	

}
