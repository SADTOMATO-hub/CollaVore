package com.collavore.app.project.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.project.mapper.ProjectTempMapper;
import com.collavore.app.project.service.PjTempService;
import com.collavore.app.project.service.ProjectDWorkTempVO;
import com.collavore.app.project.service.ProjectTempVO;
import com.collavore.app.project.service.ProjectWorkTempVO;

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
        return result == 1 ? projectTempVO.getProjTempNo() : -1;
    }
	@Override
	public int projecttempDelete(int projTempNo) {
		return ProjecttempMapper.projecttempDelete(projTempNo);
	}
	@Override
	public int projecttempUpdate(ProjectTempVO projectTempVO) {
		return ProjecttempMapper.projecttempUpdate(projectTempVO);
	}
	@Override
	public ProjectTempVO projecttempInfo(int projTempNo) {
		return ProjecttempMapper.selectProjectById(projTempNo); 
	}
	
	
	@Override
	public List<ProjectWorkTempVO> projectWrktempList() {
		return ProjecttempMapper.selectWrkTempProjectAll();
	}
	@Override
	public int projectwrktempinsert(ProjectWorkTempVO projectworktempVO) {
		 int result = ProjecttempMapper.ProjectwrkTempInsert(projectworktempVO);
	        return result == 1 ? projectworktempVO.getPwtNo() : -1;
	}
	@Override
	public int projectDelete(int pwtNo) {
		return ProjecttempMapper.projectwrktempDelete(pwtNo);
	}
	@Override
	public ProjectWorkTempVO projectwrktempInfo(int pwtNo) {
		return ProjecttempMapper.selectwrktempProject(pwtNo); 
	}
	@Override
	public int projectwrktempUpdate(ProjectWorkTempVO projectworkTempVO) {
		return ProjecttempMapper.projectwrktempUpdate(projectworkTempVO);
	}
	
	
	@Override
	public List<ProjectDWorkTempVO> projectDwrktemplist() {
		return ProjecttempMapper.selectDwrktempProjectAll();
	}
	@Override
	public int projectDwrktempinsert(ProjectDWorkTempVO projectDworktempVO) {
		 int result = ProjecttempMapper.ProjectDwrkTempInsert(projectDworktempVO);
	        return result == 1 ? projectDworktempVO.getPdwtNo() : -1;		
	}
	@Override
	public int projectdwrktempDelete(int pdwtNo) {
		return ProjecttempMapper.projectDwrktempDelete(pdwtNo);
	}
	@Override
	public ProjectDWorkTempVO projectDwrktempInfo(int pdwtNo) {
	    return ProjecttempMapper.selectDwrktempProject(pdwtNo); 
	}
	@Override
	public int projectdwrktempUpdate(ProjectDWorkTempVO projectDworktempVO) {
		return ProjecttempMapper.projectDwrktempUpdate(projectDworktempVO);
	}



}
