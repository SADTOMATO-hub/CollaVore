package com.collavore.app.cals.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.cals.mapper.SchsMapper;
import com.collavore.app.cals.service.SchsService;
import com.collavore.app.cals.service.SchsVO;

@Service
public class SchsServiceImpl implements SchsService{
	private SchsMapper schsMapper;
	
	@Autowired // 생성자 주입
	public SchsServiceImpl(SchsMapper schsMapper) {
		this.schsMapper = schsMapper;
	}
	
	//전체조회
	@Override
	public List<SchsVO> schList(){
		return schsMapper.selectSchsAll();
	}
	
	//일정등록
	@Override
	public int insertSch(SchsVO schsVO) {
		return schsMapper.insertSch(schsVO);
	}
}
