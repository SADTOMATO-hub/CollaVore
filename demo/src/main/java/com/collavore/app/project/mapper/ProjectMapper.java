package com.collavore.app.project.mapper;

import java.util.List;
import java.util.Map;

import com.collavore.app.project.service.ProjectDWorkComtVO;
import com.collavore.app.project.service.ProjectFilesVO;
import com.collavore.app.project.service.ProjectFoldersVO;
import com.collavore.app.project.service.ProjectVO;

public interface ProjectMapper {
	
	//프로젝트 조회
	public List<ProjectVO> selectProjectAll();
	// 프로젝트 생성
	public int ProjectInsert(ProjectVO projectVO);
	// 프로젝트 삭제
	public int projectDelete(int projNo);
	// 프로젝트 수정
	public int updateProject(ProjectVO projectVO);
	// 프로젝트 단건조회
	public ProjectVO selectProjectById(int projNo);
	
	public List<ProjectVO> findProjects(int offset, int size);
	
	public long countAllProjects();
	
	// 프로젝트 폴더 리스트
	public List<ProjectFoldersVO> selectfolderAll();
	// 프로젝트 파일 리스트
	public List<ProjectFilesVO> selectfileAll(int pfNo);
	
	public int fileinsert(ProjectFilesVO projectFilesVO);
	
	public ProjectFilesVO filedetail(Long projFileNo);
	
	public List<ProjectVO> projecttree();
	
	// 프로젝트 업무 생성
	public int ProjectwrkInsert(ProjectVO projectVO);
	// 프로젝트 상세업무 생성
	public int ProjectdwrkInsert(ProjectVO projectVO);
	
	// pwNo 조회하기
	public int searchPwNo(int pdwNo);
	
	// 업무 단건 조회
	public ProjectVO selectwrkInfo(int pwNo);
	// 상세업무 조회
	public ProjectVO selectdwrkInfo(int pdwNo);
	
	// 프로젝트 업무 수정
	public Map<String, Object> updatewrkProject(ProjectVO projectVO);
	// 프로젝트 상세업무 수정
	public Map<String, Object> updatedwrkProject(ProjectVO projectVO);
	
	public List<ProjectDWorkComtVO> projectDWrkComtListAll();
	
	// 프로젝트 상세업무 코멘트 단건조회
	public List<ProjectVO> searchPdwNo(int pdwNo);
	
	// 프로젝트 상세업무 코멘트 생성
	public int ProjectdwrkcomtInsert(ProjectVO projectVO);
	
	// 부서정보 조회
	public List<ProjectVO> selectdepartments();
	
	// 프로젝트 담당자 조회
	public List<ProjectVO> selectprojmgrInfo(int deptNo);
	
//	// 프로젝트 업무 담당자 조회 
//	public List<ProjectVO> selectwrkmgrInfo(); 
//	// 프로젝트 상세업무 담당자조회 
//	public List<ProjectVO> selectdwrkmgrInfo(); 
	// 업무종류 조회 
	public List<ProjectVO> selectjobs();
	// 사원 리스트 조회
	public List<ProjectVO> selectempAll();
	
	public List<ProjectVO> deteilwrkerAll();
	//  상세업무 진행상황 업데이트
	public int updateStatusProject(ProjectVO projectVO);
	 
	// 등록된 깃의 clone_url값을 받아와서 git 주소로 입력하기.
	public int insertGitUrl(ProjectVO projectVO);
	
	// 프로젝트 업무 삭제
	public int projectwrkDelete(int pwNo);
	//프로젝트 상세업무 삭제
	public int projectdwrkDelete(int pdwNo);
	
	//프로젝트 업무 리스트
	public List<ProjectVO> projwrkList(int projNo);
	
	// 상세업무 코멘트 삭제
	public int projectcomtsDelete(int pdwcNo);
	
	// 프로젝트 폴더 생성
	public int ProjectfolderInsert(ProjectVO projectVO);
	
	// 프로젝트 폴더 삭제
	public int ProjectfolderDelete(int projNo);
	
	// 프로젝트 폴더 단건 조회
	public ProjectVO projectfolderInfo(int projNo);
	
	// 프로젝트 파일 리스트 조회 
	public List<ProjectVO> projectfileInfo(Integer pfNo);
	
	// 프로젝트 파일 삭제
	public int ProjectfileDelete(Integer pfNo);
	
	// 프로젝트 업무 파일 단건 삭제
	public int projectwrkoneDelete(int pwNo);
	
	// 프로젝트 파일 삭제
	public int fileDelete(int projFileNo);
	
	
}
