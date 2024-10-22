package com.collavore.app.hrm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.hrm.mapper.MemberMapper;
import com.collavore.app.hrm.service.HrmVO;
import com.collavore.app.hrm.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {
    
    private final MemberMapper memberMapper;

    // 생성자 주입을 통해 MemberMapper를 주입 받음
    @Autowired
    public MemberServiceImpl(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    // 사원 전체 리스트 조회
    @Override
    public List<HrmVO> memberList() {
        return memberMapper.selectMemberAll();  // 데이터베이스에서 모든 사원 정보를 가져옴
    }

    // 사원 단건 조회
    @Override
    public HrmVO memberInfo(HrmVO hrmVO) {
        return memberMapper.selectMemberInfo(hrmVO);  // 사원 번호로 특정 사원 정보 조회
    }

    // 사원 등록 처리
    @Override
    public int memberInsert(HrmVO hrmVO) {
        int result = memberMapper.insertMember(hrmVO);  // 사원 정보 데이터베이스에 삽입
        return result == 1 ? hrmVO.getEmpNo() : -1;  // 등록 성공 시 사원 번호 반환, 실패 시 -1 반환
    }

    // 사원 정보 수정
    @Override
    public int memberUpdate(HrmVO hrmVO) {
        return memberMapper.updateMember(hrmVO);  // 사원 정보 수정 쿼리 실행
    }

    // 사원 삭제
    @Override
    public int memberDelete(int empNo) {
        return memberMapper.deleteMember(empNo);  // 사원 번호로 사원 정보 삭제
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
