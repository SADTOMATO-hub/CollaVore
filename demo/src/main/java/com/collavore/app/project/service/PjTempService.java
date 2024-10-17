package com.collavore.app.project.service;

import java.util.List;

public interface PjTempService {
	//프로젝트 템플릿 전체 조회
	List<ProjectTempVO> projecttempList();
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
	// 프로젝트 업무 템플릿 생성
	public int projectwrktempinsert(ProjectWorkTempVO projectworktempVO);
	// 프로젝트 업무 템플릿 삭제
	public int projectDelete(int pwtNo);
	// 
	public ProjectTempVO projectwrktempInfo(int projNo);
	
	public int projectwrktempUpdate(ProjectTempVO projectTempVO);
	
	
	
}
