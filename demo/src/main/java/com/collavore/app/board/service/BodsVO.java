package com.collavore.app.board.service;

import java.sql.Date;

import lombok.Data;

@Data
public class BodsVO {
	private Integer post_no;         //게시글 번호 
	private String title;		  //제목
	private String content;       //내용
	private Date reg_date ;		  //등록일
	private Date mod_date;		  //수정일
	private Integer board_no;      //게시판 번호
	private Integer writer;        //게시글 작성자
	
}
