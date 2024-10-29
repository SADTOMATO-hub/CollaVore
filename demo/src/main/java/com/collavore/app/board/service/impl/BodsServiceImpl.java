package com.collavore.app.board.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.board.mapper.BodsMapper;
import com.collavore.app.board.service.BodsCfigVO;
import com.collavore.app.board.service.BodsComtsVO;
import com.collavore.app.board.service.BodsService;
import com.collavore.app.board.service.BodsVO;
import com.collavore.app.hrm.service.HrmVO;

@Service
public class BodsServiceImpl implements BodsService {
	
	@Autowired
	private BodsMapper bodsMapper;

	@Autowired
	BodsServiceImpl(BodsMapper bodsMapper) {
		this.bodsMapper = bodsMapper;
	}

	// 전체 게시글 수 확인하기.
	@Override
	public int totalListCnt(BodsVO bodsVO) {
		return bodsMapper.totalBoardCnt(bodsVO);
	}

	@Override //전체조회(게시글전체조회)
	public List<BodsVO> bodsList(BodsVO bodsVO) {
		return bodsMapper.selectBoardAll(bodsVO);
	}

	@Override // 상세조회
	public BodsVO bodsInfo(BodsVO bodsVO) {
		return bodsMapper.selectBodsInfo(bodsVO);
	}

	@Override // 등록
	public int insertBods(BodsVO bodsVO) {
		int result = bodsMapper.insertBodsInfo(bodsVO);
		return result == 1 ? bodsVO.getPostNo() : -1;
	}

	@Override //수정
	public Map<String, Object> updateBods(BodsVO bodsVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;

		int result = bodsMapper.updateBodsInfo(bodsVO);
		if (result == 1) {
			isSuccessed = true;
		}

		String updateDate = getUpdateDate();

		map.put("date", updateDate);
		map.put("result", isSuccessed);
		map.put("target", bodsVO);

		return map;
	}

	private String getUpdateDate() {
		LocalDate today = LocalDate.now();
		DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String updateDt = today.format(dtFormat);
		return updateDt;

		// return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
	}

	@Override //삭제
	public int deleteBods(int bodsVO) {
		return bodsMapper.deleteBodsInfo(bodsVO);
		//return 0;
	}
	
	
	@Override //댓글등록
	public int insertBodsComts(BodsComtsVO bodsComtsVO) {
		int result = bodsMapper.insertBodsComtsInfo(bodsComtsVO);
		return result == 1 ? bodsComtsVO.getCmtNo() : -1;
	}
	
	@Override //댓글 전체조회
	public List<BodsComtsVO> bodsComtsList(int postNo) {
		return bodsMapper.selectBodsComtsAll(postNo);
	}

	@Override // 댓글 삭제
	public int deleteBodsComts(int bodsComtsVO) {
		return bodsMapper.delectBodsComtsInfo(bodsComtsVO);
	}

	
	@Override // 댓글 수정
	public Map<String, Object> updateBodsComts(BodsComtsVO bodsComtsVO) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccessed = false;

		int result = bodsMapper.updateBodsComtsInfo(bodsComtsVO);
		if (result == 1) {
			isSuccessed = true;
		}

		//String updateDate = getcomtUpdateDate();

		//map.put("date", updateDate);
		map.put("result", isSuccessed);
		map.put("target", bodsComtsVO);
		

		return map;
	}
	


	
	
	
	
	/*public String getcomtUpdateDate() {
		LocalDate today = LocalDate.now();
		DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String updateDt = today.format(dtFormat);
		return updateDt;

		// return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
	}*/

	@Override // 댓글 상세조회 
	public BodsComtsVO bodsComtsInfo(BodsComtsVO bodsComtsVO) {
		return bodsMapper.selectBodsComtsInfo(bodsComtsVO);
	}

	@Override //전체조회(게시판전체조회)
	public List<BodsCfigVO> bodsListAll(BodsCfigVO bodsCfigVO) {
		return bodsMapper.selectgbodsCfig(bodsCfigVO);
	}

	@Override //게시판 등록 boardNo로 구분 1번은 공지사항 2번 자유게시판 3번 관리자 마음대로 설정
	public int insertBodsCfig(BodsCfigVO bodsCfigVO) {
		int result = bodsMapper.insertBodsCfigInfo(bodsCfigVO);
		return result == 1 ? bodsCfigVO.getBoardNo() : -1;
	}

	
	// 게시판이름 조회
	@Override
	public String boardNameSearch(int boardNo) {
		return bodsMapper.selectBoardName(boardNo);
	}
	
	
	
}
