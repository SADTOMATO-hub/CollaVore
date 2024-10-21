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
	
//	public List<ProjectVO> getProjects(int page, int size);
//
//	public long getTotalProjects();
	
}
