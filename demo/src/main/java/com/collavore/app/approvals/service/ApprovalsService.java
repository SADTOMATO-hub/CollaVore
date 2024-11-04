package com.collavore.app.approvals.service;

import java.util.List;
import java.util.Map;

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
			public int insertApprsEa (ApprovalsVO approvalsVO);
	 		//결재자 테이블에 데이터 넣기
			public int insertApprsEar (ApprovalsVO approvalsVO);
	// 진행 중인 전자결재 목록 조회
	public List<ApprovalsVO> myApprList (ApprovalsVO approvalsVo);
	//문서함
	public List<ApprovalsVO> approveList (ApprovalsVO approvalsVo);
	// 전자결재 상세 조회
	public ApprovalsVO approvalsInfo (ApprovalsVO approvalsVO);
	//결재자 상세
	public List<Map<String,Object>> approversInfo (ApprovalsVO approvalsVo);
	//결재하기
	public int updateApprStatus (ApprovalsVO approvalsVo);	
		//전자결재 수정
	public int updateApproval (ApprovalsVO approvalsVo);
		//결재자 리스트
	public List <ApprovalsVO> approvalsList (int eaNo);
		//결재자 수정
	public int updateApprover (ApprovalsVO approvalsVo);
	// 전자결재 삭제
	public void deleteApprovals (ApprovalsVO approvalsVo);
//인사 테이블 조회
	public List<Map<String,Object>> employeesInfo ();
	
	// 결재자 삭제
	public int deleteApprover(int eaNo);
		
}
//4번째