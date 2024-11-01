package com.collavore.app.service;

import java.util.List;

public interface HomeService {
	// 게시판조회
	public List<HomeVO> selBoardList(int userGrade);
	
	// 공지사항게시글조회
	public List<HomeVO> selNoticeList();
	
	// 내가 기안한 전자결재
	public List<HomeVO> selAppList(int empNo);

    // 사원조회
    public List<HomeVO> empList();
    
    // 부서조회
    public List<HomeVO> deptAuthList();

    // 메뉴권한 없는 사원 조회
    public List<HomeVO> empAuthList(HomeVO homeVO);
    
    // 메뉴별 권한을 가진 사원 조회
    public List<HomeVO> selMenuAuthEmpList(HomeVO homeVO);
    
    // 특정사원의 특정메뉴권한 정보 조회
    public HomeVO selMenuAuthEmpInfo(HomeVO homeVO);
    
    // 사원에게 메뉴 권한 부여
    public int giveMenuAuth(HomeVO homeVO);
    
    // 사원에게 메뉴 권한 삭제
    public int removeMenuAuth(int authNo);
}
