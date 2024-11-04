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
import com.collavore.app.hrm.service.PosiService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class JobController {
	private final JobService jobService;
	
	@PostMapping("/jobs/save")
	@ResponseBody
	public String saveJobs(@RequestBody List<HrmVO> jobList) throws Exception {
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
	public String deleteJobs(@PathVariable Integer jobNo) throws Exception {
	    // 직위가 사원에게 할당되어 있는지 확인
	    if (jobService.isJobAssignedToEmployee(jobNo)) {
	        // 할당된 직위라면 삭제 불가 응답 반환
	        return "cannot_delete";
	    }

	    // 할당되지 않은 직위만 삭제 수행
	    int result = jobService.deleteJobs(jobNo);
	    return result == 1 ? "success" : "failure";
	}

	// 기존 직위 불러오기
	@GetMapping("/jobs/getExistingJobs")
	@ResponseBody
	public List<HrmVO> getExistingJobs() {
		
		return jobService.getExistinJobs();
		
	}

}
