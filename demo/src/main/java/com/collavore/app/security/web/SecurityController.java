package com.collavore.app.security.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.collavore.app.security.service.UserVO;
import com.collavore.app.security.service.impl.UserDetailsService;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class SecurityController {

    private final UserDetailsService userDetailsService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("sidemenu", "default_sidebar");
    }

    @GetMapping("/")
    public String all() {
        return "login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
