package com.collavore.app.cals.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.cals.mapper.SchsMapper;
import com.collavore.app.cals.service.SchsService;
import com.collavore.app.cals.service.SchsVO;

@Service
public class SchsServiceImpl implements SchsService {
	private final SchsMapper schsMapper;

	@Autowired // 생성자 주입
	public SchsServiceImpl(SchsMapper schsMapper) {
		this.schsMapper = schsMapper;
	}

	// 조회
	@Override
	public List<SchsVO> SchsList() {
		return schsMapper.selectSchsAll();
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
		return result == 1 ? schsVO.getSchNo() : -1;

	}
	@Override
    public Map<String, Object> updateShcs(SchsVO schsVO) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            int updatedRows = schsMapper.updateSchsInfo(schsVO);
            
            if (updatedRows > 0) {
                resultMap.put("success", true);
                resultMap.put("message", "일정이 성공적으로 수정되었습니다.");
            } else {
            	
                resultMap.put("success", false);
                resultMap.put("message", "수정할 일정이 없습니다.");
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "수정 중 오류가 발생했습니다.");
        }
        return resultMap;
    }
	
	
	// 삭제
	@Override
	public int deleteSchs(int schsNO) {
		return schsMapper.deleteSchsInfo(schsNO);
	}
}
