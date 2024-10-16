package com.collavore.app.hrm.service;

import java.util.List;
import java.util.Map;

public interface PosiService {

    /**
     * 직위 정보 삽입
     * @param hrmVO 직위 정보 객체
     * @return 삽입된 행의 수
     * @throws Exception 삽입 과정에서 발생할 수 있는 예외
     */
    public int posiInsert(HrmVO hrmVO) throws Exception;

    /**
     * 기존 직위 정보 업데이트
     * @param hrmVO 업데이트할 직위 정보 객체
     * @return 업데이트된 행의 수
     * @throws Exception 업데이트 과정에서 발생할 수 있는 예외
     */
    public int updatePosition(HrmVO hrmVO) throws Exception;

    /**
     * 직위 정보 삭제
     * @param posiNo 삭제할 직위 번호
     * @return 삭제된 행의 수
     * @throws Exception 삭제 과정에서 발생할 수 있는 예외
     */
    public int deletePosition(Integer posiNo) throws Exception;

    /**
     * 모든 직위 정보 조회
     * @return 직위 정보 리스트
     */
    public List<HrmVO> getExistingPositions();

}
