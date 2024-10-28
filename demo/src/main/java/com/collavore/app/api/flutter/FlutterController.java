package com.collavore.app.api.flutter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.collavore.app.api.service.FlutterApprVO;
import com.collavore.app.api.service.FlutterProjVO;
import com.collavore.app.api.service.FlutterSchsVO;
import com.collavore.app.api.service.FlutterService;
import com.collavore.app.api.service.FlutterVO;
import com.collavore.app.security.service.UserVO;
import com.collavore.app.security.service.impl.UserDetailsService;
import com.collavore.app.service.PdfMakeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class FlutterController {

	private final PdfMakeService pdfMakeService;
	private final FlutterService flutterService;
	private final UserDetailsService userDetailsService;
	@Value("${file.upload.path}") // 메모리에 올라가 있는 변수값을 가져오기 때문에 표현이 다름
    private String uploadPath;

	// 로그인
//	@PostMapping("/login")
//	public FlutterVO loginProcess(@RequestBody FlutterVO flutterVO) {
//		FlutterVO result = flutterService.loginChk(flutterVO);
//		return result != null ? result : new FlutterVO();
//	}

	@PostMapping("/login")
	public FlutterVO login(@RequestBody FlutterVO flutterVO, HttpSession session) {
		System.out.println(flutterVO.getEmail());
		UserVO user = userDetailsService.findByEmail(flutterVO.getEmail());

		if (user == null || !userDetailsService.authenticate(flutterVO.getPassword(), user.getPassword())) {
			// 로그인 실패 시, 빈 객체 반환
			return new FlutterVO();
		} else {
			// 로그인 성공 시, 사용자 정보를 담은 객체 반환
			FlutterVO result = new FlutterVO();
			result.setEmail(user.getEmail());
			result.setEmpNo(user.getEmpNo());
			return result;
		}
	}

	// 아이디찾기
	@PostMapping("/findId")
	public String findIdProcess(@RequestBody FlutterVO flutterVO) {
		String result = flutterService.findId(flutterVO);
		return result != null ? result : "";
	}

	// 비밀번호찾기 - 계정유무확인
	@PostMapping("/chkUser")
	public String chkUserProcess(@RequestBody FlutterVO flutterVO) {
		int result = flutterService.userChk(flutterVO);
		return result > 0 ? "Success" : "Error";
	}

	// 비밀번호찾기 - 비밀번호변경
	@PostMapping("/pwdModify")
	public String pwdModifyProcess(@RequestBody FlutterVO flutterVO) {
		int result = flutterService.pwdModify(flutterVO);
		return result > 0 ? "Success" : "Error";
	}

	// 일정목록조회
	@GetMapping("/schsSelectAll")
	public List<FlutterSchsVO> selSchsAllList(@RequestParam int empNo,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date selectDate) throws ParseException {
		List<FlutterSchsVO> mySchs = flutterService.schsAll(empNo,
				new SimpleDateFormat("yyyy-MM-dd").format(selectDate));
		return mySchs;
	}

	// 일정등록
	@PostMapping("/schsAdd")
	public int schsAdd(@RequestBody FlutterSchsVO flutterSchsVO) {
		System.out.println(flutterSchsVO);
		int result = flutterService.schsAdd(flutterSchsVO);
		return result;
	}

	// 일정상세
	@GetMapping("/schsInfo")
	public FlutterSchsVO schsInfo(@RequestParam int schNo) {
		FlutterSchsVO schsInfo = flutterService.schsInfo(schNo);
		return schsInfo;
	}

	// 일정수정

	// 프로젝트목록조회
	@GetMapping("/projSelectAll")
	public List<FlutterProjVO> selProjAllList(@RequestParam int empNo) {
		List<FlutterProjVO> myProjs = flutterService.projAll(empNo);
		return myProjs;
	}

	// 프로젝트업무목록조회
	@GetMapping("/projWorkSelectAll")
	public List<FlutterProjVO> selProjWorkAllList(@RequestParam int projNo, @RequestParam int empNo) {
		List<FlutterProjVO> myProjWorks = flutterService.projWorkAll(projNo, empNo);
		return myProjWorks;
	}

	// 프로젝트상세업무목록조회
	@GetMapping("/projWorkDetailSelectAll")
	public List<FlutterProjVO> selProjWrokDetailAllList(@RequestParam int pwNo, @RequestParam int empNo) {
		List<FlutterProjVO> myProjDetailWorks = flutterService.projWorkDetailAll(pwNo, empNo);
		return myProjDetailWorks;
	}
	// 프로젝트상세업무상세보기
	// 프로젝트상세업무상세보기-댓글조회
	// 프로젝트상세업무상세보기-댓글등록
	// 프로젝트상세업무상세보기-댓글삭제

	// 전자결재문서목록조회
	@GetMapping("/apprAll")
	public List<FlutterApprVO> selApprAllList(@RequestParam int empNo, @RequestParam String appType) {
		List<FlutterApprVO> myApprList = flutterService.apprAll(empNo, appType);
		return myApprList;
	}

	// 전자결재문서상세보기
	@GetMapping("/appInfo")
	public FlutterApprVO selAppr(@RequestParam int empNo, @RequestParam int eaNo) throws IOException {
		FlutterApprVO apprInfo = flutterService.apprInfo(empNo, eaNo);
		String pdfBase64 = downloadPdf(apprInfo.getContent());
		apprInfo.setPdfInfo(pdfBase64);
		apprInfo.setContent(null);
		return apprInfo;
	}

	// 전자결재문서를 PDF 바이트 타입으로 받기
	public String downloadPdf(String content) throws IOException {

		byte[] pdfBytes = pdfMakeService.createPdfWithHtml(content);

		return Base64.getEncoder().encodeToString(pdfBytes);
	}

	// 전자결재문서의 결재자 정보조회
	@GetMapping("/apprInfo")
	public List<FlutterApprVO> selApprovers(@RequestParam int empNo, @RequestParam int eaNo) {
		List<FlutterApprVO> result = flutterService.apprList(empNo, eaNo);
		return result;
	}

	// 전자결재문서승인,반려처리
	@PostMapping("/processApproval")
	public int modifyApprStatus(FlutterApprVO flutterApprVO) {
		int result = flutterService.apprProc(flutterApprVO);
		return result;
	}

	// 회원정보조회
	@GetMapping("/userInfo")
	public FlutterVO selectMyEmpInfo(@RequestParam int empNo) {
		FlutterVO result = flutterService.myEmpInfo(empNo);
		return result;
	}

	// 회원정보수정
	@PostMapping("/updateUserInfo")
	public int modifyMyEmpInfo(String empNo, String tel, String info, String password,
			@RequestPart(value = "img", required = false) MultipartFile img, FlutterVO flutterVO) {
		flutterVO.setEmpNo(Integer.parseInt(empNo));
		flutterVO.setTel(tel);
		flutterVO.setInfo(info);
		flutterVO.setPassword(password);
		System.out.println(img);

		// 프로필 이미지 처리 (파일이 선택된 경우에만)
		if (img != null && !img.isEmpty()) {
			try {
				// 기존 파일 경로 설정
				String originalFilename = img.getOriginalFilename();
				System.out.println(originalFilename);
				
				String fileExtension = getFileExtension(originalFilename);
				System.out.println(fileExtension);
				
				String newFileName = UUID.randomUUID().toString() + fileExtension;
				System.out.println(newFileName);
				
				String savePath = uploadPath + File.separator + newFileName;
				System.out.println(savePath);

				// 파일 저장
				Files.copy(img.getInputStream(), Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);

				// 이미지 파일명을 HrmVO 객체에 설정
				flutterVO.setProfileImg(newFileName);
				int result = flutterService.memberModify(flutterVO);
				return result;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			// 파일을 선택하지 않은 경우 기존 이미지 유지
			FlutterVO existingMember = flutterService.myEmpInfo(Integer.parseInt(empNo));
			flutterVO.setProfileImg(existingMember.getProfileImg());
		}
		int result = flutterService.memberModify(flutterVO);
		return result;
	}

	/**
	 * 파일 확장자를 추출하는 메서드
	 */
	private String getFileExtension(String fileName) {
		if (fileName != null && fileName.contains(".")) {
			return fileName.substring(fileName.lastIndexOf("."));
		}
		return "";
	}
}
