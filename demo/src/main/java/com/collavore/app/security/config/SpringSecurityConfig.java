package com.collavore.app.security.config;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.collavore.app.security.service.LoginUserVO;
import com.collavore.app.security.service.UserVO;
import com.collavore.app.security.service.impl.UserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class SpringSecurityConfig {

	private final UserDetailsService userDetailsService;

	public SpringSecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/imgs/**", "/assets/**", "/dist/**", "/smarteditor/**", "/api/**", "/").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .permitAll()
                .failureUrl("/login?error=true") 
	            .failureHandler((request, response, exception) -> {
	                String errorMessage = "아이디 또는 비밀번호가 올바르지 않습니다.";
	                request.getSession().setAttribute("loginError", errorMessage);
	                response.sendRedirect("/login?error=true");
	            })
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )  // 로그인 실패 처리
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // iframe 허용하는 명령어
            ;

        return http.build();
    }

	@Bean
	public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		return new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException {
				LoginUserVO loginUser = (LoginUserVO) authentication.getPrincipal();
				UserVO userVO = loginUser.getUserVO();

				HttpSession session = request.getSession();
				session.setAttribute("userEmpNo", userVO.getEmpNo());
				session.setAttribute("userDeptNo", userVO.getDeptNo());
				session.setAttribute("userImg", userVO.getImg());
				session.setAttribute("userGrade", userVO.getPosiGrade());
				session.setAttribute("userAdmin", userVO.getIsAdmin());

				List<String> menuAuth = userDetailsService.myMenuAuth(userVO.getEmpNo());

				session.setAttribute("menuAuth", menuAuth);

				response.sendRedirect("/dashboard");
			}
		};
	}

	@Bean
	public FilterRegistrationBean<CustomRedirectFilter> redirectFilterRegistration() {
		FilterRegistrationBean<CustomRedirectFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new CustomRedirectFilter());
		registrationBean.addUrlPatterns("/*");
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 필터 순서 설정
		return registrationBean;
	}
}