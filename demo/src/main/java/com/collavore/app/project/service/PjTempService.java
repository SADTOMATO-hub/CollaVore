package com.collavore.app.project.service;

import java.util.List;

public interface PjTempService {
	//프로젝트 템플릿 전체 조회
	List<ProjectTempVO> projecttempList();
	// 템플릿 리스트(검색)
	List<ProjectTempVO> projecttempListSearch(String searchText);
	//프로젝트 템플릿 생성
	public int projecttempinsert(ProjectTempVO projectTempVO);
	//프로젝트 템플릿 삭제
	public int projecttempDelete(int projTempNo);
	// 템플릿 수정
	public int projecttempUpdate(ProjectTempVO projectTempVO);
	// 단건 조회
	public ProjectTempVO projecttempInfo(int projTempNo);
	
	
	// 프로젝트 업무 템플릿 전체 조회
	List<ProjectWorkTempVO> projectWrktempList();
	// 프로젝트 업무 템플릿 전체 조회
	List<ProjectWorkTempVO> projectWrktempListSearch(String searchText);
	// 프로젝트 업무 템플릿 생성
	public int projectwrktempinsert(ProjectWorkTempVO projectworktempVO);
	// 프로젝트 업무 템플릿 삭제
	public int projectDelete(int pwtNo);
	// 프로젝트 업무 템플릿 단건 조회
	public ProjectWorkTempVO projectwrktempInfo(int pwtNo);
	// 프로젝트 업무 텟플릿 수정
	public int projectwrktempUpdate(ProjectWorkTempVO projectworkTempVO);
	
	
	
	// 프로젝트 상세 업무 템플릿 전체 조회
	List<ProjectDWorkTempVO> projectDwrktemplist();
	// 프로젝트 상세 업무 템플릿 전체 조회
	List<ProjectDWorkTempVO> projectDwrktemplistSearch(String searchText);
	// 프로젝트 상세 업무 템플릿 생성
	public int projectDwrktempinsert(ProjectDWorkTempVO projectDworktempVO);
	// 프로젝트 상세 업무 템플릿 삭제
	public int projectdwrktempDelete(int pdwtNo);
	// 프로젝트 상세 업무 템플릿 단건 조회
	public ProjectDWorkTempVO projectDwrktempInfo(int pdwtNo);
	// 프로젝트 상세 업무 템플릿 수정
	public int projectdwrktempUpdate(ProjectDWorkTempVO projectDworktempVO);
	// 프로젝트 업무 단건조회
	List<ProjectWorkTempVO> projectwrktemplistInfo(Integer projTempNo);
	
	List<ProjectDWorkTempVO> projectDwrktemplistInfo(Integer pwtNo);
	
	// 프로젝트 업무 템플릿 삭제 
	public int projectwrktempdel(Integer projTempNo);
	
	//  프로젝트 상세업무 템플릿 삭제
	public int  projectDwrktempdel(Integer pwtNo);
	
	
	
	
}
