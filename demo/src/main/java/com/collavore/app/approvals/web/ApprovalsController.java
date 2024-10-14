package com.collavore.app.approvals.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApprovalsController {
	@GetMapping("test")
	String test() {
		return "approvals/newPost";
	}
	
}
