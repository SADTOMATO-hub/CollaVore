package com.collavore.app.hrm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.hrm.mapper.MemberMapper;
import com.collavore.app.hrm.service.HrmVO;
import com.collavore.app.hrm.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {
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

    // 사원 단건조회
    @Override
    public HrmVO memberInfo(HrmVO hrmVO) {
        return memberMapper.selectMemberInfo(hrmVO);
    }

    // 사원 등록
    @Override
    public int memberInsert(HrmVO hrmVO) {
        int result = memberMapper.insertMember(hrmVO);
        return result == 1 ? hrmVO.getEmpNo() : -1;
    }

    // 로그인 처리 (평문 비밀번호 사용)
    @Override
    public boolean authenticate(String email, String password) {
        HrmVO user = memberMapper.findByEmail(email);  // 이메일로 사용자 정보 조회

        if (user != null && password.equals(user.getPassword())) {
            // 비밀번호가 일치하면 로그인 성공
            return true;
        }
        // 사용자 정보가 없거나 비밀번호가 틀리면 로그인 실패
        return false;
    }

    // 이메일로 사용자 정보 조회
    @Override
    public HrmVO findByEmail(String email) {
        return memberMapper.findByEmail(email);  // 이메일을 기준으로 사용자 정보 조회
    }
    
}
