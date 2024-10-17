package com.collavore.app.project.service;

import java.util.List;

public interface PjTempService {
	//프로젝트 템플릿 전체 조회
	List<ProjectTempVO> projecttempList();
	//프로젝트 템플릿 생성
	public int projecttempinsert(ProjectTempVO projectTempVO);
	
	
	
}
