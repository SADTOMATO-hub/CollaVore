package com.collavore.app.api.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.collavore.app.api.mapper.FlutterMapper;
import com.collavore.app.api.service.FlutterApprVO;
import com.collavore.app.api.service.FlutterProjVO;
import com.collavore.app.api.service.FlutterSchsVO;
import com.collavore.app.api.service.FlutterService;
import com.collavore.app.api.service.FlutterVO;

@Service
public class FlutterServiceImpl implements FlutterService {
	private final FlutterMapper flutterMapper;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	FlutterServiceImpl(FlutterMapper flutterMapper, PasswordEncoder passwordEncoder){
		this.flutterMapper = flutterMapper;
		this.passwordEncoder = passwordEncoder;
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
	public List<FlutterSchsVO> schsAll(int empNo, String dateString) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date selectDate = formatter.parse(dateString);
		return flutterMapper.selectMyAllSchsList(empNo, selectDate);
	}

	// 일정등록
	@Override
	public int schsAdd(FlutterSchsVO flutterSchsVO){
		return flutterMapper.schsInsert(flutterSchsVO);
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
	public List<FlutterApprVO> apprAll(int empNo, String appType) {
		if(appType.equals("draft")) {
			return flutterMapper.selectMyDraftApprList(empNo);
		}else if(appType.equals("approval")) {
			return flutterMapper.selectMyApprList(empNo);
		}else {
			return flutterMapper.selectMyAllApprList(empNo);
		}
	}

	// 전자결재무선상세보기
	@Override
	public FlutterApprVO apprInfo(int empNo, int eaNo) {
		return flutterMapper.selectAppInfo(eaNo);
	}
	
	// 전자결재문서 결재자보기
	@Override
	public List<FlutterApprVO> apprList(int empNo, int eaNo) {
		return flutterMapper.selectApproversList(eaNo);
	}

	// 전자결재문서승인,반려처리
	@Override
	public int apprProc(FlutterApprVO flutterApprVO) {
		return flutterMapper.updateApprStatus(flutterApprVO);
	}

	// 회원정보조회
	@Override
	public FlutterVO myEmpInfo(int empNo) {
		return flutterMapper.selectMyEmpInfo(empNo);
	}

	// 회원정보수정
	@Override
	public int memberModify(FlutterVO flutterVO) {
		// 비밀번호 암호화
		
		String insPwd = flutterVO.getPassword(); // 입력한 비밀번호 가져오기
		if(insPwd != null) {
			String encryptedPassword = passwordEncoder.encode(insPwd); // 입력한 비밀번호 암호화
			flutterVO.setPassword(encryptedPassword); // 암호화된 비밀번호 다시 VO에 넣기
		} else {
			flutterVO.setPassword(null);
		}
		return flutterMapper.updateMyEmpInfo(flutterVO);
	}

}
