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

	// 등록 - 페이지
	@GetMapping("deptInsert")
	public String deptInsertForm() {
		return "dept/insert";
	}

	// 등록 - 처리
	@PostMapping("deptInsert")
	public String deptInsertProcess(HrmVO hrmVO) {
		int did = deptService.deptInsert(hrmVO);

		String url = null;

		if (did > -1) {
			url = "redirect:deptInfo?departmentId=" + did;
		} else {
			url = "redirect:deptList";
		}
		return url;
	}

	// 수정 - 페이지
	@GetMapping("deptUpdate")
	public String deptUpdateForm(HrmVO hrmVO, Model model) {
		HrmVO findVO = deptService.deptInfo(hrmVO);
		model.addAttribute("dept", findVO);
		return "dept/update";
	}

	// 수정 - 처리 : JSON
	@PostMapping("deptUpdate")
	@ResponseBody // AJAX
	public Map<String, Object> empUpdateAJAXJSON(@RequestBody HrmVO hrmVO) {
		return deptService.deptUpdate(hrmVO);
	}

	/*
	 * // 부서 전체조회
	 * 
	 * @GetMapping("deptManager") public String deptList(Model model) { List<HrmVO>
	 * list = deptService.deptList(); model.addAttribute("depts", list); return
	 * "member/deptManager"; }
	 */

	/*
	 * // 부서 등록
	 * 
	 * @GetMapping("deptInsert") public String deptInsertForm() { return
	 * "dept/insert"; }
	 */

	
}
