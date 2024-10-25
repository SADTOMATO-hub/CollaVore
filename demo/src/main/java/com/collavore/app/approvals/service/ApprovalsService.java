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
	public int deleteTemplate(ApprovalstempVO apprsVO);
	// 전자결재 생성
		//전자결재 테이블에 데이터 넣기
	 public int insertApprsEaTable (ApprovalsVO approvalsVO);
		//결재자 테이블에 데이터 넣기
	 public int insertApprsEarTable (ApprovalsVO approvalsVO);
	// 진행 중인 전자결재 목록 조회
	// 전체 전자결재 목록 조회
	// 전자결재 상세 조회
	public ApprovalsVO approvalsInfo (ApprovalsVO approvalsVO);
	//결재자 상세
	public List<ApprovalsVO> approversInfo (ApprovalsVO approvalsVo);
	// 전자결재 수정
	// 전자결재 삭제
	public void deleteApprovals (ApprovalsVO approvalsVo);
//인사 테이블 조회
	public List<HrmVO> employeesInfo ();
}
//4번째