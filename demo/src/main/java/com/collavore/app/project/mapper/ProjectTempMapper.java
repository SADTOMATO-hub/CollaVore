package com.collavore.app.project.mapper;

import java.util.List;

import com.collavore.app.project.service.ProjectTempVO;

public interface ProjectTempMapper {

	List<ProjectTempVO> selecttempProjectAll();
	public int ProjectTmepInsert(ProjectTempVO projectTempVO);

}
