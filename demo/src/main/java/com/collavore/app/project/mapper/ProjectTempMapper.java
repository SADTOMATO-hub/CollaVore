package com.collavore.app.project.mapper;

import java.util.List;

import com.collavore.app.project.service.ProjectDWorkTempVO;
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
	// 업무 템플릿 생성
	public int ProjectwrkTempInsert(ProjectWorkTempVO projectworktempVO);
	// 업무 템플릿 삭제
	public int projectwrktempDelete(int pwtNo);
	// 업무 템플릿 단건조회
	public ProjectWorkTempVO selectwrktempProject(int pwtNo);
	// 업무 템플릿 수정
	public int projectwrktempUpdate(ProjectWorkTempVO projectworktempVO);
	
	// 상세 업무 템플릿 리스트
	List<ProjectDWorkTempVO> selectDwrktempProjectAll();
	// 상세 업무 템플릿 생성
	public int ProjectDwrkTempInsert(ProjectDWorkTempVO projectDworktempVO);
	// 상세 업무 템플릿 삭제
	public int projectDwrktempDelete(int pdwtNo);
	// 상세 업무 템플릿 단건 조회
	public ProjectDWorkTempVO selectDwrktempProject(int pdwtNo);
	// 상세 업무 템플릿 수정
	public int projectDwrktempUpdate(ProjectDWorkTempVO projectDworktempVO);
	
	List<ProjectTempVO> projectwrktemplistInfo(Integer projTempNo);
	
	List<ProjectDWorkTempVO> projectDwrktemplistInfo(Integer pwtNo);
	
	// 업무 템플릿 삭제
	public int projectwrkdelete(Integer pwtNo);
	
	// 상세업무 템플릿 삭제
	public int projectDwrkdelete(Integer pwtNo);
}
