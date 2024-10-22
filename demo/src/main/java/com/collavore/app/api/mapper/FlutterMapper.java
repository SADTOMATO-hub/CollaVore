package com.collavore.app.api.mapper;

import java.util.List;

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
	public List<FlutterSchsVO> selectMyAllSchsList(int empNo);
	
	// 일정상세
	public FlutterSchsVO selectSchsInfo(int schsNo);
	
	// 프로젝트목록조회
	public List<FlutterProjVO> selectMyAllProjList(int empNo);
	
	// 프로젝트업무목록조회
	public List<FlutterProjVO> selectMyAllProjWorkList(int projNo, int empNo);
	
	// 프로젝트상세업무목록조회
	public List<FlutterProjVO> selectMyAllProjDetailWorkList(int pwNo, int empNo);
}
