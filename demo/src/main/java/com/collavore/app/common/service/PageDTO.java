package com.collavore.app.common.service;

import lombok.Data;

@Data
public class PageDTO {
	private int page; // 현재 페이지
	int pageCnt = 10; // 페이지 당 출력되는 데이터 수
	int pageTCnt = 10; // 한 화면에 총 페이지 출력 수 (10개 페이지 출력한다는 의미)
	private int startPage, endPage; // 현재 페이지 기준 첫 페이지와 마지막 페이지 번호
	private boolean prev, next; // 이전페이지가 있는지 다음페이지가 있는지 여부를 출력

	public PageDTO(String pageStr, int totalCnt) {
		// 현재 페이지를 정수로 변환
		this.page = Integer.parseInt(pageStr);
		// 총 페이지 수 계산
		int totalPageCount = (int) Math.ceil(totalCnt / (double) pageCnt);
		// 현재 페이지의 마지막 페이지 번호 계산
		this.endPage = (int) Math.ceil(this.page / (double) pageTCnt) * pageTCnt;
		// 시작 페이지 계산
		this.startPage = this.endPage - (pageTCnt - 1);
		// 실제 데이터에 맞는 endPage 구하기
		this.endPage = Math.min(this.endPage, totalPageCount);
		// 이전 및 다음 버튼 활성화 여부 결정
		this.prev = this.startPage > 1;
		this.next = this.endPage < totalPageCount;
	}
}// end class