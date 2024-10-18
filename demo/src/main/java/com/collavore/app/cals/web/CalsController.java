package com.collavore.app.cals.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.collavore.app.cals.service.CalsService;
import com.collavore.app.cals.service.CalsVO;

public class CalsController {
	private final CalsService calsService;

	// 생성자 주입
	@Autowired
	public CalsController(CalsService calsService) {
		this.calsService = calsService;
	}
	
	
	// 조회 받은거 UI로 뿌리기
		@GetMapping("//")
		public String SchsListView() {
			return "common/sidebar/calendar_sidebar";
		}
	
	@GetMapping("/cal/Solocal")
    public List<CalsVO> getSoloCal() {
        return calsService.soloCal();
    }

    @GetMapping("/cal/teamCal")
    public List<CalsVO> getTeamCal() {
        return calsService.teamCal();
    }

    @GetMapping("/cal/projCal")
    public List<CalsVO> getProjCal() {
        return calsService.projCal();
    }
}
