package com.collavore.app.hrm.service;

import java.util.List;
import java.util.Map;

public interface JobService {
	
	public int jobsInsert(HrmVO hrmVO) throws Exception;
	public int updateJobs(HrmVO hrmVO) throws Exception;
	public int deleteJobs(Integer posiNo) throws Exception;
	public List<HrmVO> getExistingJobs();
}
