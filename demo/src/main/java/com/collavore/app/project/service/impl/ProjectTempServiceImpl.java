package com.collavore.app.project.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.project.mapper.ProjectTempMapper;
import com.collavore.app.project.service.PjTempService;
import com.collavore.app.project.service.ProjectTempVO;

@Service	//AOP => @Transcational
public class ProjectTempServiceImpl implements PjTempService{
	private ProjectTempMapper ProjecttempMapper;
	
	@Autowired // 생성자 주입
	public ProjectTempServiceImpl(ProjectTempMapper ProjecttempMapper) {
		this.ProjecttempMapper = ProjecttempMapper;
	}
	@Override
	public List<ProjectTempVO> projecttempList() {
		return ProjecttempMapper.selecttempProjectAll();
	}
	@Override
	public int projecttempinsert(ProjectTempVO projectTempVO) {
		int result = ProjecttempMapper.ProjectTmepInsert(projectTempVO);
		return result == 1? projectTempVO.getProjTempNo() : -1;
	}



}
