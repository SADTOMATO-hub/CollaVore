package com.collavore.app.board.service;

import java.sql.Date;

import lombok.Data;


@Data
public class BodsComtsVO {
	private Integer cmtNo;  // 댓글번호
	private String content;  // 댓글내용
	private Integer postNo; // 게시글 번호 FK 
	private Date regDate;   // 댓글등록일
 	private String writer;  // 댓글작성자 FK (게시글 번호)
 	private Integer empNo; //댓글작성자고유번호
	
}


