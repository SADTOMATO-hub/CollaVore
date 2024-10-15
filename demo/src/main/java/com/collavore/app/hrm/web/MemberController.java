package com.collavore.app.hrm.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.collavore.app.hrm.service.HrmVO;
import com.collavore.app.hrm.service.MemberService;

@Controller
public class MemberController {

   private MemberService memberService;

   @Autowired
   MemberController(MemberService memberService) {
      this.memberService = memberService;
   }

   @ModelAttribute
   public void addAttributes(Model model) {
      model.addAttribute("sidemenu", "member_sidebar");
   }

   
    // 테스트
    
    @GetMapping("/login") public String homepage() { 
    	return "member/login";
    }
    

   // 사원 전체조회(관리자)
   @GetMapping("memberList")
   public String memberList(Model model) {
      List<HrmVO> list = memberService.memberList();
      model.addAttribute("members", list);
      return "member/memberList";
   }

   // 사원 단건조회
   @GetMapping("memberInfo")
   public String memberInfo(HrmVO hrmVO, Model model) {
      HrmVO findVO = memberService.memberInfo(hrmVO);
      model.addAttribute("member", findVO);
      return "member/memberInfo";
   }

   // 사원 등록
   @GetMapping("memberInsert")
   public String memberInsertForm() {
      return "member/memberinsert";
   }

   // 사원 등록 처리
   @PostMapping("memberInsert")
   public String memberInsertProcess(HrmVO hrmVO) {
      int eno = memberService.memberInsert(hrmVO);

      String url = null;

      if (eno > -1) {
         url = "redirect:memberInfo?empNo=" + eno;
      } else {
         url = "redirect:memberList";
      }
      return url;
   }
   
   /*// 사원 등록 처리
   @PostMapping("memberInsert")
   public String memberInsertProcess(HrmVO hrmVO, RedirectAttributes redirectAttributes) {
       try {
           int eno = memberService.memberInsert(hrmVO);

           if (eno > -1) {
               // 성공 시, 성공 메시지와 함께 사원 정보 페이지로 리다이렉트
               redirectAttributes.addFlashAttribute("message", "사원이 성공적으로 등록되었습니다.");
               return "redirect:memberInfo?empNo=" + eno;
           } else {
               // 실패 시, 실패 메시지와 함께 등록 페이지로 리다이렉트
               redirectAttributes.addFlashAttribute("message", "사원 등록에 실패했습니다. 다시 시도해주세요.");
               return "redirect:memberInsert";
           }
       } catch (Exception e) {
           // 예외 발생 시, 예외 처리
           redirectAttributes.addFlashAttribute("errorMessage", "오류가 발생했습니다: " + e.getMessage());
           return "redirect:memberInsert";
       }
   }*/
   
   /*// 사원 수정
   @GetMapping("memberUpdate")
   public String deptUpdateForm(HrmVO hrmVO, Model model) {
      HrmVO findVO = memberService.memberInfo(hrmVO);
      model.addAttribute("member", findVO);
      return "member/memberupdate";
   }

   // 사원 수정 처리
   @PostMapping("memberUpdate")
   @ResponseBody
   public Map<String, Object> 
   empUpdateAJAXJSON(@RequestBody HrmVO hrmVO) { 
      return memberService.memberUpdate(hrmVO); 
      }*/

   /*// 사원 삭제
   @GetMapping("memberDelete")
   public String memberDelete(Integer eno) {
      memberService.memberDelete(eno);
      return "redirect:memberList";
   }*/

}
