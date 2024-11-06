package com.collavore.app.security.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

	@Value("${file.upload.url}") // 환경변수 및 Properties 파일 Read
	private String uploadUrl;
	
	@Value("${file.upload.filePath}") // 환경변수 및 Properties 파일 Read
	private String uploadFilePath;

	// 리소스 핸들링
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler(uploadUrl+"**") // URL ** => 하위 폴더
				.addResourceLocations("file://" + uploadFilePath)
				.setCacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES));  // 접근 파일 캐싱 시간; // 실제 경로

		registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");
		registry.addResourceHandler("/dist/**").addResourceLocations("classpath:/static/dist/");
		registry.addResourceHandler("/smarteditor/**").addResourceLocations("classpath:/static/smarteditor/");
	}

}