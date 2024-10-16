package com.collavore.app.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.collavore.app.api.mapper.FlutterMapper;
import com.collavore.app.api.service.FlutterService;
import com.collavore.app.api.service.FlutterVO;

@Service
public class FlutterServiceImpl implements FlutterService {
	private FlutterMapper flutterMapper;

	@Autowired
	FlutterServiceImpl(FlutterMapper flutterMapper){
		this.flutterMapper = flutterMapper;
	}
	
	@Override
	public FlutterVO loginChk(FlutterVO flutterVO) {
		return flutterMapper.selectUser(flutterVO);
	}

}
