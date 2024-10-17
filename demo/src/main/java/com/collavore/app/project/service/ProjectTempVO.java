package com.collavore.app.project.service;




import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectTempVO {
	
	//프로젝트 템플릿관리
	private Integer projTempNo;
	private String name;
	private String content;
	private Integer periodDate;
	
	

	
	
	
}
