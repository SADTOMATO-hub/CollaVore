package com.collavore.app.project.mapper;

import java.util.List;
import java.util.Map;

import com.collavore.app.project.service.ProjectFilesVO;
import com.collavore.app.project.service.ProjectFoldersVO;
import com.collavore.app.project.service.ProjectVO;

public interface ProjectMapper {
	
	//프로젝트 조회
	public List<ProjectVO> selectProjectAll();
	// 프로젝트 생성
	public int ProjectInsert(ProjectVO projectVO);
	// 프로젝트 삭제
	public int projectDelete(int projNo);
	// 프로젝트 수정
	public Map<String, Object> updateProject(ProjectVO projectVO);
	// 프로젝트 단건조회
	public ProjectVO selectProjectById(int projNo);
	
	public List<ProjectVO> findProjects(int offset, int size);
	
	public long countAllProjects();
	
	public List<ProjectFoldersVO> selectfileAll();
}
