package com.collavore.app.project.service;

import java.util.List;
import java.util.Map;


public interface PjService {
	//프로젝트현황 리스트 
	public List<ProjectVO> projectList();
	//프로젝트 생성
	public int projectinsert(ProjectVO proejctVO);
	// 프로젝트 삭제
	public int projectDelete(int projNo);
	// 프로젝트 단건조회
	public ProjectVO projectInfo(int projNo); 
	// 프로젝트 수정
	public Map<String, Object> updateProject(ProjectVO proejctVO);
	
	// 프로젝트 폴더 리스트 
	public List<ProjectFoldersVO> projectfolderList();
	// 프로젝트 파일 리스트
	public List<ProjectFilesVO> projectfileList(int pfNo);
	
	public int saveFile(String originalFilename, ProjectFilesVO projectFilesVO);
	
	public ProjectFilesVO getFileDetails(Long projFileNo);
	
	// 프로젝트 트리구조 리스트
	public List<ProjectVO> projecttreeList();
	
	public int  projectwrkinsert(ProjectVO projectVO);
	
	public int selectPwNo(int pdwNo);
	
	public int projectdwrkinsert(ProjectVO projectVO);
	
	
	public ProjectVO projectwrkInfo(int pwNo);
	
	public ProjectVO projectdwrkInfo(int pdwNo);
	
	public Map<String, Object> updatewrkProject(ProjectVO projectVO);
	
	public Map<String, Object> updatedwrkProject(ProjectVO projectVO);
	
	// 프로젝트 상세업무 코멘트 전체 리스트
	public List<ProjectDWorkComtVO> projectDWrkComtList();
	
	// 프로젝트 상세업무 코멘트 단건조회 
//	public List<ProjectVO> projectDWrkComtInfo(int pdwNo);
	public List<ProjectVO> projectDWrkComtInfo(int pdwNo);
	
	// 프로젝트 상세업무 코멘트 생성
	public int projectdwrkcomtinsert(ProjectVO projectVO);
	
	
//	public List<ProjectVO> getProjects(int page, int size);
//
//	public long getTotalProjects();
	
}
