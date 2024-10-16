package com.collavore.app.approvals.service;

import java.util.List;

public interface ApprovalsService {
	// 템플릿 생성
	public int createApprsTemp (ApprovalstempVO apprsVO);
	// 템플릿 목록 조회
	public List<ApprovalstempVO> apprTempList ();
	// 템플릿 상세 조회
	public ApprovalstempVO apprInfo (ApprovalstempVO eatNo);
	// 템플릿 수정
	// 템플릿 삭제
	public int DeleteTemplate(ApprovalstempVO eatNo);
	// 전자결재 생성
	// 진행 중인 전자결재 목록 조회
	// 전체 전자결재 목록 조회
	// 전자결재 상세 조회
	// 전자결재 수정
	// 전자결재 삭제
}
//4번째