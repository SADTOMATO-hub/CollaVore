package com.collavore.app.approvals.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.approvals.mapper.ApprovalsMapper;
import com.collavore.app.approvals.service.ApprovalsService;
import com.collavore.app.approvals.service.ApprovalstempVO;

@Service
public class ApprovalsImpl implements ApprovalsService {
	//mapper와 연결
	private ApprovalsMapper approvalsMapper;
		//생성자
	@Autowired
	public ApprovalsImpl(ApprovalsMapper approvalsMapper) {
		this.approvalsMapper = approvalsMapper;
	}
		//메소드
	// 템플릿 목록 조회
	@Override
	public List<ApprovalstempVO> apprTempList() {
		return approvalsMapper.tempList();
	}
	// 템플릿 상세 조회
	@Override
	public ApprovalstempVO apprInfo(ApprovalstempVO apprsVO) {
		return approvalsMapper.readTemp(apprsVO);
	}
	// 템플릿 생성
	@Override
	public int createApprsTemp(ApprovalstempVO apprsVO) {
		int result = approvalsMapper.createApprsTemp(apprsVO);
		return result == 1 ? apprsVO.getEatNo() : -1 ;
	}
	// 템플릿 수정
	@Override
	public Map<String, Object> updateTemplate(ApprovalstempVO apprsVO) {
		Map<String, Object> map = new HashMap<>();
		int result = approvalsMapper.updateTemp(apprsVO);
		boolean doneOrNot = false;
				if(result == 1) {
					doneOrNot = true;
				}
		map.put("result", doneOrNot);
		map.put("tempUpdate", apprsVO);
		return map;
	}
	// 템플릿 삭제
	@Override
	public int DeleteTemplate(ApprovalstempVO apprsVO) {
		int result = approvalsMapper.deleteTemp(apprsVO);
		return result == 1 ? apprsVO.getEatNo() : -1;
	}
	// 전자결재 생성
	// 진행 중인 전자결재 목록 조회
	// 전체 전자결재 목록 조회
	// 전자결재 상세 조회
	// 전자결재 수정
	// 전자결재 삭제
}
//5번째