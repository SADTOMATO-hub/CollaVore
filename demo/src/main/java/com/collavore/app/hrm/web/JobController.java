package com.collavore.app.hrm.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.hrm.service.HrmVO;
import com.collavore.app.hrm.service.JobService;

@Controller
public class JobController {
	private JobService jobService;

	@Autowired
	JobController(JobService jobService) {
		this.jobService = jobService;
	}

	
	@PostMapping("/jobs/save")
	@ResponseBody
	public String saveJobtions(@RequestBody List<HrmVO> jobList) throws Exception {
	    System.out.println("Received data: " + jobList); // 받은 데이터를 출력

	    int result = 0;

	    for (HrmVO hrmVO : jobList) {
	        System.out.println("Processing: " + hrmVO);  // 처리 중인 데이터 출력
	        if (hrmVO.getJobNo() != null) {
	            // 기존 데이터 업데이트
	            result += jobService.updateJobs(hrmVO);
	        } else {
	            // 새로운 데이터 등록
	            result += jobService.jobsInsert(hrmVO);
	        }
	        System.out.println("Current result: " + result);
	    }

	    return result > 0 ? "success" : "failure";
	}

	// 직위 삭제 처리
	@DeleteMapping("/jobs/delete/{jobNo}")
	@ResponseBody
	public String deleteJobtion(@PathVariable Integer jobNo) throws Exception {
		int result = jobService.deleteJobs(jobNo);
		return result == 1 ? "success" : "failure";
	}

	// 기존 직위 불러오기
	@GetMapping("/jobs/getExistingJobs")
	@ResponseBody
	public List<HrmVO> getExistingJobtions() {
		
		return jobService.getExistinJobs();
		
	}

}
