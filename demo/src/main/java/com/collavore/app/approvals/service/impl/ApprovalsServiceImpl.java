package com.collavore.app.approvals.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collavore.app.approvals.mapper.ApprovalsMapper;
import com.collavore.app.approvals.service.ApprovalsService;
import com.collavore.app.approvals.service.ApprovalsVO;
import com.collavore.app.approvals.service.ApprovalstempVO;
import com.collavore.app.hrm.service.HrmVO;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Service
public class ApprovalsServiceImpl implements ApprovalsService {
	// mapper와 연결
	private ApprovalsMapper approvalsMapper;

		// 생성자
	@Autowired
	public ApprovalsServiceImpl(ApprovalsMapper approvalsMapper) {
		this.approvalsMapper = approvalsMapper;
	}

		// 메소드
	// 템플릿 목록 조회, 템플릿의 모든 정보를 불러오는 기능
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
		return result == 1 ? apprsVO.getEatNo() : -1;
	}

	// 템플릿 수정
	@Override
	public int updateTemplate(ApprovalstempVO apprsVO) {
		return approvalsMapper.updateTemp(apprsVO);
	}

	// 템플릿 삭제
	@Override
	public int deleteTemplate(ApprovalstempVO apprsVO) {
		int result = approvalsMapper.deleteTemp(apprsVO);
		return result == 1 ? apprsVO.getEatNo() : -1;
	}

	// 전자결재 생성
		// 전자결재 
	@Override
	@Transactional
	public int insertApprsEa(ApprovalsVO apprVO) {
		int EaNo = approvalsMapper.createApprsEa(apprVO);
		return EaNo > 0 ? apprVO.getEaNo() : -1;
	}
		// 결재자
	@Override
	@Transactional
	public int insertApprsEar(ApprovalsVO apprVO) {
		//결재번호를 부른다. 다건
		for (ApprovalsVO appr : apprVO.getApprovers()) {
			appr.setEaNo(apprVO.getEaNo());
		}
		int result = approvalsMapper.createApprsEar(apprVO);
		return result;
	}

	// 진행 중인 전자결재 목록 조회
	@Override
	public List<ApprovalsVO> myApprList(ApprovalsVO approvalsVo) {
		return approvalsMapper.myApprList(approvalsVo);
	}
	
	//문서함
	@Override
	public List<ApprovalsVO> approveList(ApprovalsVO approvalsVo) {
		return approvalsMapper.approveList(approvalsVo);
	}
	
	// 전자결재 상세 조회
	@Override
	public ApprovalsVO approvalsInfo(ApprovalsVO approvalsVO) {
		return approvalsMapper.readApproval(approvalsVO);
	}
	
	// 결재하기
	@Override
	public int updateApprStatus(ApprovalsVO approvalsVo) {
		int result = approvalsMapper.updateApprStatus(approvalsVo);
		return result;
	}

	// 결재자 상세 조회
	@Override
	public List<Map<String,Object>> approversInfo(ApprovalsVO approvalsVo) {
		return approvalsMapper.readApprovers(approvalsVo);
	}
	
	// 전자결재 수정
	@Override
	public int updateApproval(ApprovalsVO approvalsVo) {
		int result = approvalsMapper.updateApproval(approvalsVo);
		return result;
	}
	@Override
	public List<ApprovalsVO> approvalsList(int eaNo) {
		return approvalsMapper.approvalsList(eaNo);
	}
	
	// 전자결재 삭제
	@Override
	public void deleteApprovals(ApprovalsVO approvalsVo) {
		approvalsMapper.deleteApproval(approvalsVo);
	}
	
	// 인사 테이블
	@Override
	public List<HrmVO> employeesInfo(int userEmpNo, int deptNo) {
		return approvalsMapper.employees(userEmpNo, deptNo);
	}
	
	// 부서 테이블 조회
	@Override
	public List<HrmVO> depts() {
		return approvalsMapper.depts();
	}
	
	// 결재자 삭제
	@Override
	public int deleteApprover(int eaNo) {
		return approvalsMapper.deleteApprover(eaNo);
	}
}
//5번째