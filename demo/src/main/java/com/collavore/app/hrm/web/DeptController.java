package com.collavore.app.hrm.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
	private DeptService deptService;

	@Autowired
	DeptController(DeptService deptService) {
		this.deptService = deptService;
	}

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("sidemenu", "member_sidebar");
	}

	// 부서 전체조회
	@GetMapping("deptManager")
	public String deptList(Model model) {
		List<HrmVO> list = deptService.deptList();
		model.addAttribute("depts", list);
		return "member/deptManager";
	}

}
