package com.collavore.app.api.mapper;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.collavore.app.api.service.FlutterApprVO;
import com.collavore.app.api.service.FlutterProjVO;
import com.collavore.app.api.service.FlutterSchsVO;
import com.collavore.app.api.service.FlutterVO;

public interface FlutterMapper {
	// 로그인 시도
	public FlutterVO selectUser(FlutterVO flutterVO);

	// 아이디찾기
	public String selectId(FlutterVO flutterVO);
	
	// 비밀번호찾기-계정유무확인
	public int chkUser(FlutterVO flutterVO);
	
	// 비밀번호찾기-비밀번호변경
	public int updatePassword(FlutterVO flutterVO);
	
	// 일정목록조회
	public List<FlutterSchsVO> selectMyAllSchsList(int empNo, @DateTimeFormat(pattern = "yyyy-MM-dd") Date selectDate);
	
	// 일정등록
	public int schsInsert(FlutterSchsVO flutterSchsVO);
	
	// 일정상세
	public FlutterSchsVO selectSchsInfo(int schsNo);
	
	// 프로젝트목록조회
	public List<FlutterProjVO> selectMyAllProjList(int empNo);
	
	// 프로젝트업무목록조회
	public List<FlutterProjVO> selectMyAllProjWorkList(int projNo, int empNo);
	
	// 프로젝트상세업무목록조회
	public List<FlutterProjVO> selectMyAllProjDetailWorkList(int pwNo, int empNo);
	
	// 전자결재조회(전체)
	public List<FlutterApprVO> selectMyAllApprList(int empNo);

	// 전자결재조회(기안)
	public List<FlutterApprVO> selectMyDraftApprList(int empNo);

	// 전자결재조회(결재)
	public List<FlutterApprVO> selectMyApprList(int empNo);
	
	// 전자결재상세보기
	public FlutterApprVO selectAppInfo(int eaNo);
}
