package com.collavore.app.project.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.project.mapper.ProjectMapper;
import com.collavore.app.project.service.PjService;
import com.collavore.app.project.service.ProjectDWorkComtVO;
import com.collavore.app.project.service.ProjectFilesVO;
import com.collavore.app.project.service.ProjectFoldersVO;
import com.collavore.app.project.service.ProjectVO;

@Service	
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
    public int updateProject(ProjectVO projectVO) {
        return projectMapper.updateProject(projectVO);
    }
    // 프로젝트 폴더 리스트
	@Override
	public List<ProjectFoldersVO> projectfolderList() {
		return projectMapper.selectfolderAll();
	}
	// 프로젝트 파일 리스트
	@Override
	public List<ProjectFilesVO> projectfileList(int pfNo) {
		return projectMapper.selectfileAll(pfNo);
	}

	@Override
	public int saveFile(String originalFilename, ProjectFilesVO projectFilesVO) {
		int result = projectMapper.fileinsert(projectFilesVO);
		return result == 1? projectFilesVO.getProjFileNo() : -1;
	}
	public ProjectFilesVO getFileDetails(Long projFileNo) {
	    return projectMapper.filedetail(projFileNo);
	}

	@Override
	public List<ProjectVO> projecttreeList() {
		return projectMapper.projecttree();
	}

	@Override
	public int projectwrkinsert(ProjectVO projectVO) {
		int result = projectMapper.ProjectwrkInsert(projectVO);
		return result;
	}

	@Override
	public int projectdwrkinsert(ProjectVO projectVO) {
		int result = projectMapper.ProjectdwrkInsert(projectVO);
		return result;
	}
	// pdwNo를 기반으로 pwNo 조회하기
	
	@Override
	public int selectPwNo(int pdwNo) {
		int result = projectMapper.searchPwNo(pdwNo);
		return result;
	}

	@Override
	public ProjectVO projectwrkInfo(int pwNo) {
		return projectMapper.selectwrkInfo(pwNo); 
	}

	@Override
	public ProjectVO projectdwrkInfo(int pdwNo) {
		return projectMapper.selectdwrkInfo(pdwNo); 
	}

	@Override
	public Map<String, Object> updatewrkProject(ProjectVO projectVO) {
		return projectMapper.updatewrkProject(projectVO);
	}

	@Override
	public Map<String, Object> updatedwrkProject(ProjectVO projectVO) {
		return projectMapper.updatedwrkProject(projectVO);
	}

	@Override
	public List<ProjectDWorkComtVO> projectDWrkComtList() {
		return projectMapper.projectDWrkComtListAll();
	}

	@Override
	public List<ProjectVO> projectDWrkComtInfo(int pdwNo) {
	    return projectMapper.searchPdwNo(pdwNo); 
	}

	@Override
	public int projectdwrkcomtinsert(ProjectVO projectVO) {
		int result = projectMapper.ProjectdwrkcomtInsert(projectVO);
		return result;
	}

	@Override
	public List<ProjectVO> departmentsList() {
		return projectMapper.selectdepartments();
	}



	@Override
	public List<ProjectVO> projectMgrListInfo(int jobNo) {
		 return projectMapper.selectprojmgrInfo(jobNo); 
	}

	@Override
	public List<ProjectVO> jobsList() {
		 return projectMapper.selectjobs(); 
	}

	@Override
	public List<ProjectVO> empList() {
		return projectMapper.selectempAll();
	}

	@Override
	public List<ProjectVO> deteilwrkerList() {
		return projectMapper.deteilwrkerAll();
	}

	@Override
	public int updatestatusProject(ProjectVO projectVO) {
		return projectMapper.updateStatusProject(projectVO);
	}

	/*
	 * @Override public List<ProjectVO> projectDWrkComtInfo(int pdwNo) { return
	 * projectMapper.searchPdwNo(pdwNo); }
	 */

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
	

	// 등록된 깃의 clone_url값을 받아와서 git 주소로 입력하기.
	@Override
	public int addGitUrl(ProjectVO projectVO) {
		return projectMapper.insertGitUrl(projectVO);
	}

	@Override
	public int projectwrkDelete(int pwNo) {
		return projectMapper.projectwrkDelete(pwNo);
	}

	@Override
	public int projectdwrkDelete(int pdwNo) {
		return projectMapper.projectdwrkDelete(pdwNo);
	}

	@Override
	public List<ProjectVO> projectwrkList(int projNo) {
		return projectMapper.projwrkList(projNo);
	}

	@Override
	public int projectcomtsDelete(int pdwcNo) {
		return projectMapper.projectcomtsDelete(pdwcNo);
	}

	@Override
	public int projectfolderinsert(ProjectVO projectVO) {
		return projectMapper.ProjectfolderInsert(projectVO);
	}

	@Override
	public int projectfolderDelete(int projNo) {
		return projectMapper.ProjectfolderDelete(projNo);
	}

	@Override
	public ProjectVO projectfolderInfo(int projNo) {
		return projectMapper.projectfolderInfo(projNo); 
	}

	@Override
	public List<ProjectVO> projfileinfo(Integer pfNo) {
		return projectMapper.projectfileInfo(pfNo); 
	}

	@Override
	public int projfiledel(Integer pfNo) {
		return projectMapper.ProjectfileDelete(pfNo); 
	}
}
