package com.collavore.app.approvals.mapper;

import java.util.List;

import com.collavore.app.approvals.service.ApprovalsVO;
import com.collavore.app.approvals.service.ApprovalstempVO;
import com.collavore.app.hrm.service.HrmVO;

public interface ApprovalsMapper {
	//템플릿 목록 조회, 템플릿의 모든 정보를 불러오는 기능
	public List<ApprovalstempVO> tempList ();
	//템플릿 상세 조회
	public ApprovalstempVO readTemp (ApprovalstempVO apprsVO);
	//템플릿 생성
	public int createApprsTemp (ApprovalstempVO apprsVO);
	//템플릿 수정
	public int updateTemp (ApprovalstempVO apprsVO);
	//템플릿 삭제
	public int deleteTemp (ApprovalstempVO apprsVO);
	//전자결재 생성
		//전자결재 테이블에 데이터 넣기
		public int createApprsEaTable (ApprovalsVO approvalsVo);
		//결재자 테이블에 데이터 넣기
		public int createApprsEarTable (ApprovalsVO approvalsVo);
	//진행 중인 전자결재 목록 조회
	//전체 전자결재 목록 조회
	//전자결재 상세 조회
	public ApprovalsVO readApproval (ApprovalsVO approvalsVo);
	//결재자 상세
	public List<ApprovalsVO> readApprovers (ApprovalsVO approvalsVo);
	//전자결재 수정
	//전자결재 삭제
	public void deleteApproval (ApprovalsVO approvalsVo);
	//인사 테이블 조회
	public List<HrmVO> employees ();
}
//2번