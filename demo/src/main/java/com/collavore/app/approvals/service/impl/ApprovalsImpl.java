package com.collavore.app.approvals.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.collavore.app.approvals.mapper.ApprovalsMapper;
import com.collavore.app.approvals.service.ApprovalsService;
import com.collavore.app.approvals.service.ApprovalsVO;
import com.collavore.app.approvals.service.ApprovalstempVO;
import com.collavore.app.hrm.service.HrmVO;

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
		return result == 1 ? apprsVO.getEatNo() : -1 ;
	}
	// 템플릿 수정
	@Override
	public int updateTemplate(ApprovalstempVO apprsVO) {
		//Map<String, Object> map = new HashMap<>();
	//	int result = approvalsMapper.updateTemp(apprsVO);
		//if (result == 1) {
		//	map.put("tempUpdate", apprsVO);			
		//}
		//return map;
		return approvalsMapper.updateTemp(apprsVO);
	}
	// 템플릿 삭제
	@Override
	public int deleteTemplate(ApprovalstempVO apprsVO) {
		int result = approvalsMapper.deleteTemp(apprsVO);
		return result == 1 ? apprsVO.getEatNo() : -1;
	}
	// 전자결재 생성
		//전자결재 테이블
	@Override
	@Transactional
	public int createApprsEaTable(ApprovalsVO approvalsVO) {
		int result = approvalsMapper.createApprsEaTable(approvalsVO);
		return result;
	}
		//전자결재자 테이블
	@Override
	@Transactional
	public int createApprsEarTable(ApprovalsVO approvalsVO) {
		int result = approvalsMapper.createApprsEarTable(approvalsVO);
		return result;						
	}
	// 진행 중인 전자결재 목록 조회
	// 전체 전자결재 목록 조회
	// 전자결재 상세 조회
	// 전자결재 수정
	// 전자결재 삭제
	// 결재자의 정보를 호출
	//결재자 이름을 검색하면 그 대상의 정보를 호출하는 기능
	@Override
	public List<HrmVO> employeesInfo( ) {
		return approvalsMapper.employeesInfo();
	}
}
//5번째