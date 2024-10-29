package com.collavore.app.board.service;

import java.sql.Date;

import lombok.Data;

@Data
public class BodsVO {
	private Integer postNo;         //게시글 번호 
	private String title;		  //제목
	private String content;       //내용
	private Date regDate ;		  //등록일
	private Date modDate;		  //수정일
	private Integer boardNo;      //게시판 번호
	private Integer writer;        //게시글 작성자
	private String writerName; //작성자이름
	private String page; // 페이지 번호
	
	private String boardName; // 게시판 이름
}
