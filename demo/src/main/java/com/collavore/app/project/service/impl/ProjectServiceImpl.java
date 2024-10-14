package com.collavore.app.project.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.project.mapper.ProjectMapper;
import com.collavore.app.project.service.PjService;
import com.collavore.app.project.service.ProjectVO;

@Service	//AOP => @Transcational
public class ProjectServiceImpl implements PjService{
	private ProjectMapper projectMapper;
	
	@Autowired // 생성자 주입
	public ProjectServiceImpl(ProjectMapper projectMapper) {
		this.projectMapper = projectMapper;
	}
	
	@Override
	public List<ProjectVO> projectList() {
		return projectMapper.selectProjectAll();
	}

}
