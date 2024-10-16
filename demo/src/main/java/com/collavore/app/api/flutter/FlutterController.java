package com.collavore.app.api.flutter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.api.service.FlutterService;
import com.collavore.app.api.service.FlutterVO;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FlutterController {

	private final FlutterService flutterService;

	@PostMapping("api/login")
	@ResponseBody
	public FlutterVO loginProcess2(@RequestBody FlutterVO flutterVO, Model model) {
		FlutterVO result = flutterService.loginChk(flutterVO);
		
		if(result != null) {
			
		} else {
			
		}

		return result != null ? result : new FlutterVO();
	}
}
