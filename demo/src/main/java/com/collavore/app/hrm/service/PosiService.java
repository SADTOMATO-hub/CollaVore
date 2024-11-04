package com.collavore.app.hrm.service;

import java.util.List;
import java.util.Map;

public interface PosiService {
	int posiInsert(HrmVO hrmVO) throws Exception;

    public int updatePosition(HrmVO hrmVO) throws Exception;

    public int deletePosition(Integer posiNo) throws Exception;

    public List<HrmVO> getExistingPositions();
    
    public boolean isPositionAssignedToEmployee(Integer posiNo);

}
