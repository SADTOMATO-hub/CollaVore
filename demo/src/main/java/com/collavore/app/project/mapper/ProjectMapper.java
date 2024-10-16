package com.collavore.app.project.mapper;

import java.util.List;

import com.collavore.app.project.service.ProjectVO;

public interface ProjectMapper {
	
	//프로젝트 조회
	public List<ProjectVO> selectProjectAll();
	// 프로젝트 생성
	public int ProjectInsert(ProjectVO projectVO);
	
	// 프로젝트 삭제
	public int projectDelete(int projNo);
}
