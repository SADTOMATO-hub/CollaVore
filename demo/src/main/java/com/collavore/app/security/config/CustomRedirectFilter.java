package com.collavore.app.security.config;
import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomRedirectFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/")) {
            response.sendRedirect("/myPage"); // 루트("/")로 접근 시 리다이렉트
            return;
        }
        filterChain.doFilter(request, response); // 다른 요청은 필터 체인으로 전달
    }
}
