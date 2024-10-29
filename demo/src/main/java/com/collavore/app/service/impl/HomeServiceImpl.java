package com.collavore.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.mapper.HomeMapper;
import com.collavore.app.service.HomeService;
import com.collavore.app.service.HomeVO;

@Service
public class HomeServiceImpl implements HomeService {

	private final HomeMapper homeMapper;

	// 생성자 주입을 통해 MemberMapper를 주입 받음
	@Autowired
	public HomeServiceImpl(HomeMapper homeMapper) {
		this.homeMapper = homeMapper;
	}

	// 게시판조회
	@Override
	public List<HomeVO> selBoardList() {
		return homeMapper.selectBoardList();
	}

	// 공지사항게시글 조회
	@Override
	public List<HomeVO> selNoticeList() {
		return homeMapper.selectNoticeList();
	}

	// 내가 기안한 전자결재
	@Override
	public List<HomeVO> selAppList(int empNo) {
		return homeMapper.selectAppList(empNo);
	}

}
