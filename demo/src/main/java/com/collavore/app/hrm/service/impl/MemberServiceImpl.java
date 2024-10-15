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

   // 사원 단건조회
   @Override
   public HrmVO memberInfo(HrmVO hrmVO) {
      return memberMapper.selectMemberInfo(hrmVO);
   }

   // 사원 등록
   @Override
   public int memberInsert(HrmVO hrmVO) {
      int result = memberMapper.insertMember(hrmVO);
      return result == 1 ? hrmVO.getEmpNo() : -1 ;
   }

}
