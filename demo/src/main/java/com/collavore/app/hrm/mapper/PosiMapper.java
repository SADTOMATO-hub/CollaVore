package com.collavore.app.hrm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.collavore.app.hrm.service.HrmVO;

public interface PosiMapper {

	public int insertPosiInfo(HrmVO hrmVO);

	public int updatePosiInfo(HrmVO hrmVO);

	public int deletePosiInfo(Integer posiNo);

	public List<HrmVO> selectPosiList();
	
	public int countEmployeesWithPosition(Integer posiNo);
}
