package com.collavore.app.hrm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.hrm.mapper.MemberMapper;
import com.collavore.app.hrm.service.MemberService;
import com.collavore.app.hrm.service.HrmVO;

@Service
public class MemberServiceImpl implements MemberService{
	private MemberMapper memberMapper;
	
	@Autowired
	public MemberServiceImpl(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}
	
	// 사원 전체 리스트
	@Override
	public List<HrmVO> memberList() {
		return memberMapper.selectMemberAll();
	}

}
