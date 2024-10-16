package com.collavore.app.cals.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.cals.mapper.SchsMapper;
import com.collavore.app.cals.service.SchsService;
import com.collavore.app.cals.service.SchsVO;

@Service
public class SchsServiceImpl implements SchsService {
	private final SchsMapper schsMapper;

	@Autowired // 생성자 주입
	public SchsServiceImpl(SchsMapper schsMapper) {
		this.schsMapper = schsMapper;
	}

	@Override
	public List<SchsVO> SchsList() {
		return schsMapper.selectSchsAll();
	}

	// 등록
	@Override
	public int insertSchs(SchsVO schsVO) {
		int result = schsMapper.insertSchsInfo(schsVO);
		return result == 1 ? schsVO.getSchNo() : -1;

	}
}
