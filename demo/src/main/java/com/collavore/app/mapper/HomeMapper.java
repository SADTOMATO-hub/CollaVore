package com.collavore.app.mapper;

import java.util.List;

import com.collavore.app.service.HomeVO;

public interface HomeMapper {
	//게시판조회
	public List<HomeVO> selectBoardList(int userGrade);
	
	// 공지사항게시글조회
	public List<HomeVO> selectNoticeList();
	
	// 내가 기안한 전자결재
	public List<HomeVO> selectAppList(int empNo);
	
	// 회원조회
	public List<HomeVO> selectEmpList();
	
	// 부서조회
	public List<HomeVO> selectDeptList();
	
	// 메뉴권한 없는 사원 조회
	public List<HomeVO> empAuthList(HomeVO homeVO);
	
	// 메뉴별 권한을 가진 사원 조회
	public List<HomeVO> selectAuthEmpList(HomeVO homeVO);
	
	// 메뉴별 권한을 가진 사원 조회(단일)
	public HomeVO selectAuthEmpInfo(HomeVO homeVO);
	
	// 사원에게 메뉴 권한 부여
	public int insertMenuAuthEmp(HomeVO homeVO);
	
	// 사원에게 메뉴 권한 삭제
	public int deleteMenuAuth(int authNo);
	
	// 내 업무 조회
	public List<HomeVO> selectTodoList(int empNo);
	
	// 재직중인 사원 수
	public int selectCountEmp();
	
	// 프로젝트 전체 수
	public int selectCountProj();
	
	// 프로젝트 진행 수 
	public int selectCountProjIng();
	
	// 진행중인 업무 수
	public int selectCountPdw();
		
}

