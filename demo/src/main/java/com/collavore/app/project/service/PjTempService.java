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
	// 프로젝트 업무 템플릿 단건 조회
	public ProjectWorkTempVO projectwrktempInfo(int pwtNo);
	// 프로젝트 업무 텟플릿 수정
	public int projectwrktempUpdate(ProjectWorkTempVO projectworkTempVO);
	
	
	
	// 프로젝트 상세 업무 템플릿 전체 조회
	List<ProjectDWorkTempVO> projectDwrktemplist();
	// 프로젝트 상세 업무 템플릿 생성
	public int projectDwrktempinsert(ProjectDWorkTempVO projectDworktempVO);
	// 프로젝트 상세 업무 템플릿 삭제
	public int projectdwrktempDelete(int pdwtNo);
	
	
	
}
