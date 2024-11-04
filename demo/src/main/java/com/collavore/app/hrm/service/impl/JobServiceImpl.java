package com.collavore.app.hrm.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.hrm.mapper.JobMapper;
import com.collavore.app.hrm.service.HrmVO;
import com.collavore.app.hrm.service.JobService;

@Service
public class JobServiceImpl implements JobService {
	private JobMapper jobMapper;

	@Autowired
	JobServiceImpl(JobMapper jobMapper) {
		this.jobMapper = jobMapper;
	}

	// 직위
	@Override
	public int jobsInsert(HrmVO hrmVO) {
		return jobMapper.insertJobsInfo(hrmVO);
	}

	@Override
	public int updateJobs(HrmVO hrmVO) {
		return jobMapper.updateJobsInfo(hrmVO);
	}

	@Override
	public int deleteJobs(Integer jobNo) {
		return jobMapper.deleteJobsInfo(jobNo);
	}

	@Override
	public List<HrmVO> getExistinJobs() {
		return jobMapper.selectJobsList();
	}

	@Override
	public boolean isJobAssignedToEmployee(Integer jobNo) {
		return jobMapper.countEmployeesWithJob(jobNo) > 0;
	}
}