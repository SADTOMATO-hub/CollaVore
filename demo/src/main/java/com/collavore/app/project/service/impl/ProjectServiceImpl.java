package com.collavore.app.project.service.impl;


import java.util.List;
import java.util.Map;

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

	@Override
	public int projectinsert(ProjectVO proejctVO) {
		int result = projectMapper.ProjectInsert(proejctVO);
		return result == 1? proejctVO.getProjNo() : -1;
	}

	@Override
	public int projectDelete(int projNo) {
		return projectMapper.projectDelete(projNo);
	}

	@Override
	public ProjectVO projectInfo(int projNo) {
	    return projectMapper.selectProjectById(projNo); 
	}

    @Override
    public Map<String, Object> updateProject(ProjectVO projectVO) {
        return projectMapper.updateProject(projectVO);
    }

//    public List<ProjectVO> getProjects(int page, int size) {
//        // 페이지 번호와 크기에 맞춰 데이터 조회
//        int offset = (page - 1) * size;
//        return projectMapper.findProjects(offset, size); 
//    }
//
//    public long getTotalProjects() {
//        // 총 프로젝트 수 조회
//        return projectMapper.countAllProjects(); 
//    }
	

}
