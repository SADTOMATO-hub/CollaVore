package com.collavore.app.board.service;

import java.sql.Date;

import lombok.Data;

@Data
public class BodsComtsVO {
	private Integer cmtNo ;
	private String content ;
	private Integer postNo ;
	private Date regDate ;
	private Integer writer;
}
