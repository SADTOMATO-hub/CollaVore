package com.collavore.app.board.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.board.service.BodsService;
import com.collavore.app.board.service.BodsVO;

@Controller
public class BodsController {

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("sidemenu", "board_sidebar");
	}

	private BodsService bodsService;

	@Autowired
	public BodsController(BodsService bodsService) {
		this.bodsService = bodsService;
	}

	// 전체조회 : URI - boardList / RETURN - board/boardList
	@GetMapping("/board/bodsList") // 인터넷창에 치는 주소
	public String bodsList(Model model) {
		List<BodsVO> list = bodsService.bodsList();
		model.addAttribute("bodsList", list);
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
	public String bodsInfo(BodsVO bodsVO, Model model) {
		BodsVO findVO = bodsService.bodsInfo(bodsVO);
		model.addAttribute("bods", findVO);
		return "board/bodsInfo";
	}

	// 등록 - 페이지 : URI - boardInsert / RETURN - board/boardInsert
	@GetMapping("/board/bodsInsert")
	public String boardInsertForm() {
		return "/board/bodsInsert";
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
		model.addAttribute("bods", findVO);
		return "board/bodsUpdate";
	}

	// 수정 - 처리 : URI - boardUpdate / PARAMETER - BoardVO(JSON)
	// RETURN - 수정결과 데이터(Map)
	// => 등록(내부에서 수행하는 쿼리문 - UPDATE문)
	@PostMapping("/bodsUpdate")
	@ResponseBody
	public Map<String, Object> boardUpdateProcess(@RequestBody BodsVO bodsVO) {
		return bodsService.updateBods(bodsVO);
	}

	// 삭제 - 처리 : URI - boardDelete / PARAMETER - Integer
	// RETURN - 전체조회 다시 호출
	@GetMapping("/bodsDelete") // QueryString : @RequestParam
	public String boardDelete(@RequestParam Integer no) {
		bodsService.deleteBods(no);
		return "redirect:bodsList";
	}
}
