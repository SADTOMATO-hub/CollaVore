package com.collavore.app.hrm.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    // 로그인 처리 (평문 비밀번호 사용)
    @Override
    public boolean authenticate(String email, String password) {
        HrmVO user = memberMapper.findByEmail(email); // 이메일로 사용자 정보 조회

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
        return memberMapper.findByEmail(email); // 이메일을 기준으로 사용자 정보 조회
    }

    // 사원 단건 조회
    @Override
    public HrmVO memberInfo(HrmVO hrmVO) {
        return memberMapper.selectMemberInfo(hrmVO); // 사원 번호로 특정 사원 정보 조회
    }

    // 사원 정보 수정
    @Override
    public int memberUpdate(HrmVO hrmVO) {
        return memberMapper.updateMember(hrmVO); // 사원 정보 수정 쿼리 실행
    }

    // 관리자 영역 ─────────────────────────────────────────
    // 사원 전체 조회
    @Override
    public List<HrmVO> selectMemberAll() {
        return memberMapper.selectMemberAll();
    }

    // 사원 등록
    @Override
    public int insertMember(HrmVO hrmVO) {
        // 자동 생성된 사번을 설정하고 사원을 등록
        Integer empNo = generateEmpNo();  // 사번 생성 메서드 호출
        hrmVO.setEmpNo(empNo);
        return memberMapper.insertMember(hrmVO);
    }

    // 사원 삭제
    @Override
    public int deleteMember(int empNo) {
        return memberMapper.deleteMember(empNo);
    }

    // 사번 자동 생성 로직 (정수형으로 처리)
    public Integer generateEmpNo() {
        // 오늘 날짜에서 YYMMDD 형식의 날짜를 가져옵니다.
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        // 해당 날짜로 시작하는 가장 큰 사번을 조회 (YYYYMMDD 형태로)
        Integer maxEmpNo = memberMapper.getMaxEmpNo(today);

        // 시퀀스가 존재하면 +1, 아니면 100부터 시작
        int sequence = (maxEmpNo != null) ? (maxEmpNo % 1000) + 1 : 100;

        // YYMMDD + 3자리 시퀀스를 결합한 사번 생성 (예: 241022100)
        return Integer.parseInt(today + String.format("%03d", sequence));
    }
}
