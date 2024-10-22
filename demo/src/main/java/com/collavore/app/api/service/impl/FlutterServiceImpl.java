package com.collavore.app.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.api.mapper.FlutterMapper;
import com.collavore.app.api.service.FlutterProjVO;
import com.collavore.app.api.service.FlutterSchsVO;
import com.collavore.app.api.service.FlutterService;
import com.collavore.app.api.service.FlutterVO;

@Service
public class FlutterServiceImpl implements FlutterService {
	private FlutterMapper flutterMapper;

	@Autowired
	FlutterServiceImpl(FlutterMapper flutterMapper){
		this.flutterMapper = flutterMapper;
	}
	
	// 로그인
	@Override
	public FlutterVO loginChk(FlutterVO flutterVO) {
		return flutterMapper.selectUser(flutterVO);
	}

	// 아이디찾기
	@Override
	public String findId(FlutterVO flutterVO) {
		return flutterMapper.selectId(flutterVO);
	}

	// 비밀번호찾기 - 계정유무확인
	@Override
	public int userChk(FlutterVO flutterVO) {
		return flutterMapper.chkUser(flutterVO);
	}
	
	// 비밀번호찾기 - 비밀번호변경
	@Override
	public int pwdModify(FlutterVO flutterVO) {
		return flutterMapper.updatePassword(flutterVO);
	}

	// 일정목록조회
	@Override
	public List<FlutterSchsVO> schsAll(int empNo) {
		return flutterMapper.selectMyAllSchsList(empNo);
	}

	// 일정등록
	@Override
	public int schsAdd(FlutterVO flutterVO) {
		return 0;
	}

	// 일정상세
	@Override
	public FlutterSchsVO schsInfo(int schsNo) {
		return flutterMapper.selectSchsInfo(schsNo);
	}

	// 일정수정
	@Override
	public int schsModify(FlutterVO flutterVO) {
		return 0;
	}

	// 프로젝트목록조회
	@Override
	public List<FlutterProjVO> projAll(int empNo) {
		return flutterMapper.selectMyAllProjList(empNo);
	}

	// 프로젝트업무목록조회
	@Override
	public List<FlutterProjVO> projWorkAll(int projNo, int empNo) {
		return flutterMapper.selectMyAllProjWorkList(projNo, empNo);
	}

	// 프로젝트상세업무목록조회
	@Override
	public List<FlutterProjVO> projWorkDetailAll(int pwNo, int empNo) {
		return flutterMapper.selectMyAllProjDetailWorkList(pwNo, empNo);
	}

	// 프로젝트상세업무상세보기
	@Override
	public FlutterVO projWorkDetailInfo(FlutterVO flutterVO) {
		return null;
	}

	// 프로젝트상세업무상세보기-댓글조회
	@Override
	public List<FlutterVO> projCommentAll(FlutterVO flutterVO) {
		return null;
	}

	// 프로젝트상세업무상세보기-댓글등록
	@Override
	public int projCommentAdd(FlutterVO flutterVO) {
		return 0;
	}

	// 프로젝트상세업무상세보기-댓글삭제
	@Override
	public int projCommentRemove(FlutterVO flutterVO) {
		return 0;
	}

	// 전자결재문서목록조회
	@Override
	public List<FlutterVO> apprAll(FlutterVO flutterVO) {
		return null;
	}

	// 전자결재무선상세보기
	@Override
	public FlutterVO apprInfo(FlutterVO flutterVO) {
		return null;
	}

	// 전자결재문서승인,반려처리
	@Override
	public int apprProc(FlutterVO flutterVO) {
		return 0;
	}

	// 회원정보수정
	@Override
	public FlutterVO memberModify(FlutterVO flutterVO) {
		return null;
	}

}
