package com.collavore.app.hrm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.collavore.app.hrm.service.HrmVO;

public interface JobMapper {
	public int insertJobsInfo(HrmVO hrmVO);

	public int updateJobsInfo(HrmVO hrmVO);

	public int deleteJobsInfo(Integer posiNo);

	public List<HrmVO> selectJobsList();
}


