package com.collavore.app.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

	@Value("${file.upload.path}") // 환경변수 및 Properties 파일 Read
	private String uploadPath;

	// 리소스 핸들링
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/imgs/**") // URL ** => 하위 폴더
				.addResourceLocations("file:///" + uploadPath); // 실제 경로

		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");
		registry.addResourceHandler("/dist/**").addResourceLocations("classpath:/static/dist/");
		registry.addResourceHandler("/smarteditor/**").addResourceLocations("classpath:/static/smarteditor/");
	}

}