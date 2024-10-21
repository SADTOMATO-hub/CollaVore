package com.collavore.app.common.service;

import lombok.Data;

@Data
public class SearchDTO {
	private int page;
	private String searchCondition;
	private String keyword;
	private int no;
}// end class