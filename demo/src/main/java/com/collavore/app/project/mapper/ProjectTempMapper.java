package com.collavore.app.project.mapper;

import java.util.List;

import com.collavore.app.project.service.ProjectTempVO;
import com.collavore.app.project.service.ProjectWorkTempVO;

public interface ProjectTempMapper {
	// 템플릿 리스트
	List<ProjectTempVO> selecttempProjectAll();
	// 템플릿 생성
	public int ProjectTmepInsert(ProjectTempVO projectTempVO);
	// 템플릿 삭제
	public int projecttempDelete(int projTempNo);
	// 템플릿 수정
	public int projecttempUpdate(ProjectTempVO projectTempVO);
	// 단건 조회
	public ProjectTempVO selectProjectById(int projTempNo);
	
	// 업무 템플릿 리스트
	List<ProjectWorkTempVO> selectWrkTempProjectAll();
	
	public int ProjectwrkTempInsert(ProjectWorkTempVO projectworktempVO);

	// 템플릿 삭제
	public int projectwrktempDelete(int pwtNo);
	public ProjectTempVO selectwrktempProject(int pwtNo);
	public int projectwrktempUpdate(ProjectTempVO projectTempVO);
}
