package com.collavore.app.approvals.service.impl;

import org.springframework.stereotype.Service;

import com.collavore.app.approvals.mapper.ApprovalsMapper;
import com.collavore.app.approvals.service.ApprovalsService;
import com.collavore.app.approvals.service.ApprovalstempVO;

@Service
public class ApprovalsImpl implements ApprovalsService {
	//mapper와 연결
	private ApprovalsMapper approvalsMapper;
	// 템플릿 생성
	@Override
	public int createApprsTemp(ApprovalstempVO apprsVO) {
		int result = approvalsMapper.createApprsTemp(apprsVO);
		return result;
	}
	// 템플릿 목록 조회
	// 템플릿 상세 조회
	// 템플릿 수정
	// 템플릿 삭제
	// 전자결재 생성
	// 진행 중인 전자결재 목록 조회
	// 전체 전자결재 목록 조회
	// 전자결재 상세 조회
	// 전자결재 수정
	// 전자결재 삭제
}
//5번째