package com.collavore.app.approvals.service;

import java.util.List;

import com.collavore.app.hrm.service.HrmVO;

public interface ApprovalsService {
	// 템플릿 생성
	public int createApprsTemp (ApprovalstempVO apprsVO);
	// 템플릿 목록 조회, 템플릿의 모든 정보를 불러오는 기능
	public List<ApprovalstempVO> apprTempList ();
	// 템플릿 상세 조회
	public ApprovalstempVO apprInfo (ApprovalstempVO apprsVO);
	// 템플릿 수정
	public int updateTemplate (ApprovalstempVO apprsVO);
	// 템플릿 삭제
	public int deleteTemplate(ApprovalstempVO eatNo);
	// 전자결재 생성
		//전자결재 테이블에 데이터 넣기
	public int createApprsEaTable (ApprovalsVO approvalsVO);
		//결재자 테이블에 데이터 넣기
	public int createApprsEarTable (ApprovalsVO approvalsVO);
	// 진행 중인 전자결재 목록 조회
	// 전체 전자결재 목록 조회
	// 전자결재 상세 조회
	// 전자결재 수정
	// 전자결재 삭제
	//결재자 이름을 검색하면 그 대상의 정보를 호출하는 기능
	public List<HrmVO> employeesInfo ();
}
//4번째