package com.collavore.app.hrm.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.hrm.service.DeptService;
import com.collavore.app.hrm.service.HrmVO;

@Controller
public class DeptController {

    private final DeptService deptService;
    private static final Logger logger = LoggerFactory.getLogger(DeptController.class);

    @Autowired
    DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("sidemenu", "member_sidebar");
    }

    // 관리자 페이지 부서 파트 메인
    @GetMapping("deptManager")
    public String deptManager() {
        return "member/deptManager";
    }
    
    // 부서 정보 저장 처리
    @PostMapping("/dept/save")
    @ResponseBody
    public String saveDepts(@RequestBody List<HrmVO> deptList) throws Exception {
        logger.info("Received data: {}", deptList); // 받은 데이터를 로깅

        int result = 0;

        for (HrmVO hrmVO : deptList) {
            if (hrmVO.getDeptNo() != null) {
                // 기존 부서 정보 업데이트
                logger.info("Updating department: {}", hrmVO);
                result += deptService.updateDept(hrmVO);
            } else {
                // 새로운 부서 정보 등록
                logger.info("Inserting new department: {}", hrmVO);
                result += deptService.insertDept(hrmVO);
            }
        }

        // 결과 확인
        logger.info("Save operation result: {}", result);
        return result > 0 ? "success" : "failure";
    }

    // 조직 삭제 처리
    @DeleteMapping("/dept/delete/{deptNo}")
    @ResponseBody
    public String deleteDepts(@PathVariable Integer deptNo) throws Exception {
        logger.info("Deleting department with deptNo: {}", deptNo);
        
        try {
            int result = deptService.deleteDept(deptNo);
            if (result == 1) {
                logger.info("Successfully deleted department with deptNo: {}", deptNo);
                return "success";
            } else {
                logger.warn("Failed to delete department with deptNo: {}", deptNo);
                return "failure";
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting department with deptNo: {}", deptNo, e);
            return "error";
        }
    }

    // 기존 조직 불러오기
    @GetMapping("/dept/getExistingDepts")
    @ResponseBody
    public List<HrmVO> getExistingDepts() {
        logger.info("Fetching existing departments");
        return deptService.getExistingDepts();
    }
}
