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
	public List<HomeVO> selBoardList(int userGrade) {
		return homeMapper.selectBoardList(userGrade);
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


	// 부서조회
	@Override
	public List<HomeVO> deptAuthList() {
		return homeMapper.selectDeptList();
	}
	
    // 사원조회
	@Override
    public List<HomeVO> empList(){
    	return homeMapper.selectEmpList();
    }
	
    // 메뉴권한 없는 사원 조회
	@Override
    public List<HomeVO> empAuthList(HomeVO homeVO){
    	return homeMapper.empAuthList(homeVO);
    }
    
    
    // 메뉴별 권한을 가진 사원 조회
	@Override
	public List<HomeVO> selMenuAuthEmpList(HomeVO homeVO) {
		return homeMapper.selectAuthEmpList(homeVO);
	}

	// 사원에게 메뉴 권한 부여
	@Override
	public int giveMenuAuth(HomeVO homeVO) {
		return homeMapper.insertMenuAuthEmp(homeVO);
	}

	// 특정사원의 특정메뉴권한 정보 조회
	@Override
	public HomeVO selMenuAuthEmpInfo(HomeVO homeVO) {
		return homeMapper.selectAuthEmpInfo(homeVO);
	}

	// 사원에게 메뉴 권한 삭제
	@Override
	public int removeMenuAuth(int authNo) {
		return homeMapper.deleteMenuAuth(authNo);
	}

	// 내 업무 조회
	@Override
	public List<HomeVO> selTodoList(int empNo) {
		return homeMapper.selectTodoList(empNo);
	}

}
