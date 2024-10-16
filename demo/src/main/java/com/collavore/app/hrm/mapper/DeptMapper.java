package com.collavore.app.hrm.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.collavore.app.hrm.service.HrmVO;

public interface DeptMapper {
	// 전체조회
	public List<HrmVO> selectDeptAll();

	// 단건조회
	public HrmVO selectDeptInfo(HrmVO hrmVO);

	// 등록
	public int insertDeptInfo(HrmVO hrmVO);

	// 수정
	public int updateDeptInfo(@Param("did") int deptId, @Param("dept") HrmVO hrmVO);

	// 삭제
	public int deleteDeptInfo(int deptNo);

	// 직위
	public int insertPosiInfo(HrmVO hrmVO);

	public int updatePosiInfo(HrmVO hrmVO);

	public int deletePosiInfo(Integer posiNo);

	public List<HrmVO> selectPosiList();
	// 직무 전체조회
	// 직무 등록
	// 직무 수정
	// 직무 삭제
}
