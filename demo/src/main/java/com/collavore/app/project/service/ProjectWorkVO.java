package com.collavore.app.project.service;

import java.util.Date;
import lombok.Data;

@Data
public class ProjectWorkVO {

	// 프로젝트 업무관리
	private Integer pwNo;
	private String name;
	private String content;
	private String workType;
	private Integer mgrNo;
	private Date regDate;
	private Integer projNo;
	private Integer sort;

}
