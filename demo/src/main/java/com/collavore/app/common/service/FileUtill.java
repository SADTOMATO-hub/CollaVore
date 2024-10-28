package com.collavore.app.common.service;

public class FileUtill {

	// 파일 확장자 추출 메소드
	public static String getFileExtension(String originalFilename) {
		int lastIndex = originalFilename.lastIndexOf('.');
		return (lastIndex == -1) ? "" : originalFilename.substring(lastIndex + 1);
	}
	
}
