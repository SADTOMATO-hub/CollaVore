package com.collavore.app.hrm.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.hrm.service.HrmVO;
import com.collavore.app.hrm.service.PosiService;

@Controller
public class PosiController {
	private PosiService posiService;

	@Autowired
	PosiController(PosiService posiService) {
		this.posiService = posiService;
	}

	
	// 직위 등록 및 수정 처리

	@PostMapping("/save")
	@ResponseBody
	@Transactional
	public String savePositions(@RequestBody List<HrmVO> posiList) throws Exception {
		System.out.println("Received data: " + posiList); // 서버에서 받은 데이터를 출력

		int result = 0;

		for (HrmVO hrmVO : posiList) {
			if (hrmVO.getPosiNo() != null) {
				// 기존 데이터 업데이트
				result += posiService.updatePosition(hrmVO);
			} else {
				// 새로운 데이터 등록
				result += posiService.posiInsert(hrmVO);
			}
		}

		return result > 0 ? "success" : "failure";
	}

	// 직위 삭제 처리
	@DeleteMapping("/delete/{posiNo}")
	@ResponseBody
	public String deletePosition(@PathVariable Integer posiNo) throws Exception {
		int result = posiService.deletePosition(posiNo);
		return result == 1 ? "success" : "failure";
	}

	// 기존 직위 불러오기
	@GetMapping("/getExistingPositions")
	@ResponseBody
	public List<HrmVO> getExistingPositions() {
		return posiService.getExistingPositions();
	}

}
