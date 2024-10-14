package com.collavore.app.project.mapper;

import java.util.List;

import com.collavore.app.project.service.ProjectVO;

public interface ProjectMapper {
	
	//프로젝트 조회
	public List<ProjectVO> selectProjectAll();

}
