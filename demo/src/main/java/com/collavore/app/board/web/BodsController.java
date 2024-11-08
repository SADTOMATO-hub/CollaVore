package com.collavore.app.board.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.board.service.BodsCfigVO;
import com.collavore.app.board.service.BodsComtsVO;
import com.collavore.app.board.service.BodsService;
import com.collavore.app.board.service.BodsVO;
import com.collavore.app.common.service.PageDTO;
import com.collavore.app.service.HomeService;
import com.collavore.app.service.HomeVO;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BodsController {
	private final BodsService bodsService;
	private final HomeService homeService;


	@ModelAttribute
	public void addAttributes(BodsCfigVO bodsCfigVO, Model model, HttpSession session) {
		List<HomeVO> employeesInfo = homeService.empList();
		model.addAttribute("employees", employeesInfo);
		
		String userAdmin = (String) session.getAttribute("userAdmin");
		model.addAttribute("userAdmin", userAdmin);
		
		Integer userGrade = (Integer) session.getAttribute("userGrade");
		model.addAttribute("userGrade", userGrade);

		@SuppressWarnings("unchecked")
		List<String> menuAuth = (List<String>) session.getAttribute("menuAuth");
		model.addAttribute("menuAuth", menuAuth);
		
		bodsCfigVO.setPosiGrade(userGrade);
		List<BodsCfigVO> list = bodsService.bodsListAll(bodsCfigVO);
		model.addAttribute("bodsCfigList", list);
		model.addAttribute("sidemenu", "board_sidebar");
	}
	
	// 전체조회 : URI - boardList / RETURN - board/boardList
	@GetMapping("/board/bodsList") // 인터넷창에 치는 주소
	public String bodsList(BodsVO bodsVO, Model model, HttpSession session) {
		Integer userGrade = (Integer) session.getAttribute("userGrade");
		model.addAttribute("userGrade", userGrade);
		
		String page = bodsVO.getPage() == null ? "1" : bodsVO.getPage();

		BodsCfigVO boardInfo = bodsService.boardNameSearch(bodsVO.getBoardNo());
		boardInfo.setSubjectList(Arrays.asList(boardInfo.getSubject().split(",")));
		
		model.addAttribute("boardInfo", boardInfo);

		BodsCfigVO bodsCfigVO = new BodsCfigVO();
		bodsCfigVO.setBoardNo(bodsVO.getBoardNo());
		bodsCfigVO = bodsService.bodsCfigInfo(bodsCfigVO);
		model.addAttribute("bodsCfig", bodsCfigVO);
		
		List<BodsVO> list = bodsService.bodsList(bodsVO);
		model.addAttribute("bodsList", list);

		int totalCnt = bodsService.totalListCnt(bodsVO);
		PageDTO pageing = new PageDTO(page, totalCnt);
		model.addAttribute("pageing", pageing);

		return "board/bodsList"; // 불러오는 html 경로
// prefix  + return + suffix
// classpath:/templates/  +  board/boardList  +  .html
	}

	// 단건조회 : URI - boardInfo / PARAMETER - BoardVO(QueryString)
	// RETURN - board/boardInfo
	// QueryString
	// 1) 객체 : 커맨드 객체
	// 2) 단일값 : @ReqeustParam
	@GetMapping("/board/bodsInfo")
	public String bodsInfo(BodsVO bodsVO, Model model, HttpSession session) {
		Integer empNo = (Integer) session.getAttribute("userEmpNo");
		model.addAttribute("empNo", empNo);
		
		Integer userGrade = (Integer) session.getAttribute("userGrade");
		model.addAttribute("userGrade", userGrade);
		
		BodsVO findVO = bodsService.bodsInfo(bodsVO);
		model.addAttribute("bods", findVO);

		BodsCfigVO boardInfo = bodsService.boardNameSearch(findVO.getBoardNo());
		boardInfo.setSubjectList(Arrays.asList(boardInfo.getSubject().split(",")));
		model.addAttribute("boardInfo", boardInfo);

		BodsCfigVO bodsCfigVO = new BodsCfigVO();
		bodsCfigVO.setBoardNo(findVO.getBoardNo());
		bodsCfigVO = bodsService.bodsCfigInfo(bodsCfigVO);
		model.addAttribute("bodsCfig", bodsCfigVO);
		
		return "board/bodsInfo";
	}

	// 등록 - 페이지 : URI - boardInsert / RETURN - board/boardInsert
	@GetMapping("/board/bodsInsert")
	public String boardInsertForm(BodsVO bodsVO, Model model, HttpSession session) {
		Integer empNo = (Integer) session.getAttribute("userEmpNo");
		model.addAttribute("empNo", empNo);

		BodsCfigVO boardInfo = bodsService.boardNameSearch(bodsVO.getBoardNo());
		boardInfo.setSubjectList(Arrays.asList(boardInfo.getSubject().split(",")));
		model.addAttribute("boardInfo", boardInfo);

		return "board/bodsInsert";
	}

	// 등록 - 처리 : URI - boardInsert / PARAMETER - BoardVO(QueryString)
	// RETURN - 단건조회 다시 호출
	@PostMapping("/board/bodsInsert")
	public String bodsInsertProcess(BodsVO bodsVO) {// <form/> 활용한 submit
		int eid = bodsService.insertBods(bodsVO);
		return "redirect:bodsInfo?postNo=" + eid;
	}

	// 수정 - 페이지 : URI - boardUpdate / PARAMETER - BoardVO(QueryString)
	// RETURN - board/boardUpdate
	// => 단건조회
	@GetMapping("/board/bodsUpdate")
	public String boardUpdateForm(BodsVO bodsVO, Model model) {
		BodsVO findVO = bodsService.bodsInfo(bodsVO);

		BodsCfigVO boardInfo = bodsService.boardNameSearch(findVO.getBoardNo());
		boardInfo.setSubjectList(Arrays.asList(boardInfo.getSubject().split(",")));
		model.addAttribute("boardInfo", boardInfo);
		
		model.addAttribute("bods", findVO);
		return "board/bodsUpdate";
	}

	// 수정 - 처리 : URI - boardUpdate / PARAMETER - BoardVO(JSON)
	// RETURN - 수정결과 데이터(Map)
	// => 등록(내부에서 수행하는 쿼리문 - UPDATE문)
	@PostMapping("/board/bodsUpdate")
	@ResponseBody
	public Map<String, Object> boardUpdateProcess(@RequestBody BodsVO bodsVO) {
		return bodsService.updateBods(bodsVO);
	}

	// 삭제 - 처리 : URI - boardDelete / PARAMETER - Integer
	// RETURN - 전체조회 다시 호출
	@GetMapping("board/bodsDelete") // QueryString : @RequestParam
	public String bodsDelete(@RequestParam Integer postNo, @RequestParam Integer boardNo) {
		bodsService.deleteBods(postNo);

		return "redirect:bodsList?boardNo=" + boardNo; 
	}

	// 댓글등록 - 처리 : URI - boardInsert / PARAMETER - BoardVO(QueryString)
	// RETURN - 단건조회 다시 호출
	@PostMapping("/board/bodsComts")
	@ResponseBody
	public BodsComtsVO bodsComtsInsertProcess(BodsComtsVO bodsComtsVO) {// <form/> 활용한 submit
		int eid = bodsService.insertBodsComts(bodsComtsVO);
		System.out.println(bodsComtsVO);
		return bodsComtsVO;
	}

	// 댓글 삭제
	@PostMapping("/board/bodsComtsDelete")
	@ResponseBody
	public boolean bodsComtsDelete(@RequestParam Integer cmtNo) {
		int result = bodsService.deleteBodsComts(cmtNo);
		return result > 0 ? true : false;
	}

	// 댓글 조회
	@GetMapping("/board/comtsList")
	@ResponseBody
	public List<BodsComtsVO> comtsList(int postNo) {
		List<BodsComtsVO> list = bodsService.bodsComtsList(postNo);
		return list;
	}

	// 댓글 상세조회 : URI - boardInfo / PARAMETER - BoardVO(QueryString)
	@GetMapping("/board/bodscomtsInfo")
	public String bodsComtsInfo(@RequestParam BodsComtsVO bodsComtsVO) {
		bodsService.bodsComtsInfo(bodsComtsVO);
		return "board/bodsComtsInfo";
	}

	// 댓글 수정
	@PostMapping("/board/bodsComtsUpdate")
	@ResponseBody
	public Map<String, Object> boardUpdateForm(@RequestBody BodsComtsVO bodsComtsVO) {
		Map<String, Object> result = bodsService.updateBodsComts(bodsComtsVO);
		return result;
	}

	// 게시판 전체 목록 조회
	@GetMapping("/board/bodsCfig") // 인터넷창에 치는 주소
	public String bodsCfigList(BodsCfigVO bodsCfigVO, Model model) {
		// String page = bodsCfigVO.getPage() == null ? "1" : bodsCfigVO.getPage();

		List<BodsCfigVO> list = bodsService.bodsListAll(bodsCfigVO);
		model.addAttribute("bodsCfigList", list);
		
		// 직위 조회
		List<BodsCfigVO> posiList = bodsService.selPosiList();
		model.addAttribute("posiList", posiList);
		
		/*
		 * int totalCnt = bodsService.bodsListAll(bodsCfigVO); PageDTO pageing = new
		 * PageDTO(page, totalCnt); model.addAttribute("pageing", pageing);
		 */
		return "board/bodsCfig";
	}

	// 게시판 등록 페이지
	@GetMapping("/board/bodsCfigInsert")
	public String bodsCfigInsertForm(BodsCfigVO bodsCfigVO) {
		return "board/bodsCfigInsert";
	}

	// 게시판 등록 처리
	//// RETURN - 단건조회 다시 호출 /board/bodsCfig
	@PostMapping("/board/bodsCfig")
	@ResponseBody
	public BodsCfigVO bodsCfigInsertProcess(BodsCfigVO bodsCfigVO) {
		int eid = bodsService.insertBodsCfig(bodsCfigVO);
		System.out.println(bodsCfigVO);
		return bodsCfigVO;

	}

	// 게시판 상세보기
	@GetMapping("/board/bodsCfigInfo")
	public String bodsCfigInfo(BodsCfigVO bodsCfigVO, Model model, HttpSession session) {
		Integer boardNo = (Integer) session.getAttribute("userboardNo");

		if (bodsCfigVO.getBoardNo() == null) {
			bodsCfigVO.setBoardNo(boardNo);
		}
		model.addAttribute("boardNo", boardNo);
		
		BodsCfigVO findVO = bodsService.bodsCfigInfo(bodsCfigVO);
		model.addAttribute("bodsCfig", findVO);

		// 직위 조회
		List<BodsCfigVO> posiList = bodsService.selPosiList();
		model.addAttribute("posiList", posiList);
		
		return "board/bodsCfigInfo";
	}

	// 게시판 수정
	// 수정 - 페이지 : URI - boardUpdate / PARAMETER - BoardVO(QueryString)
	// RETURN - board/boardUpdate
	// => 단건조회
	@GetMapping("/board/bodsCfigUpdate")
	public String bodsCfigUpdateForm(BodsCfigVO bodsCfigVO, Model model) {
		BodsCfigVO findVO = bodsService.bodsCfigInfo(bodsCfigVO);
		model.addAttribute("bodsCfig", findVO);

		// 직위 조회
		List<BodsCfigVO> posiList = bodsService.selPosiList();
		model.addAttribute("posiList", posiList);
		return "board/bodsCfigUpdate";
	}

	// 수정 - 처리 : URI - boardUpdate / PARAMETER - BoardVO(JSON)
	// RETURN - 수정결과 데이터(Map)
	// => 등록(내부에서 수행하는 쿼리문 - UPDATE문)
	@PostMapping("/board/bodsCfigUpdate")
	@ResponseBody
	public Map<String, Object> bodsCfigUpdateProcess(@RequestBody BodsCfigVO bodsCfigVO) {
		return bodsService.updateBodsCfig(bodsCfigVO);
	}
	
	// 게시판 삭제 
	@GetMapping("board/bodsCfigDelete") // QueryString : @RequestParam
	public String bodsCfigDelete(@RequestParam Integer boardNo) {
		bodsService.deleteBodsCfig(boardNo);

		return "redirect:/board/bodsCfig";
	}
	
	
	

}
