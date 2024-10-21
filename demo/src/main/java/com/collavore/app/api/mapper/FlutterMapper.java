package com.collavore.app.api.mapper;

import java.util.List;

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
	
	public List<FlutterSchsVO> selectMyAllSchsList(int empNo);
	
	public FlutterSchsVO selectSchsInfo(int schsNo);
	
}
