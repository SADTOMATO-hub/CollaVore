package com.collavore.app.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

@Service
public class PdfMakeService {

	public byte[] createPdfWithHtml(String htmlContent) throws IOException {
		
        String fontPath = "src/main/resources/fonts/NotoSansCJKkr-VF.ttf";
        
		String wrappedContent = "<!DOCTYPE html><html><head><style>"
				+ "body { font-family: 'Noto Sans KR', sans-serif; }"
				+ "table { width: 100%; border-collapse: collapse; }"
				+ "th, td { border: 1px solid #000; padding: 8px; text-align: left; }" + "</style></head><body>"
				+ htmlContent + "</body></html>";

		String sanitizedContent = sanitizeHtml(wrappedContent);
		System.out.println(sanitizedContent);
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			PdfRendererBuilder builder = new PdfRendererBuilder();

			builder.withHtmlContent(sanitizedContent, null);
			
            builder.useFont(new File(fontPath), "Noto Sans KR");
            
			builder.useFastMode();

			builder.toStream(baos);
			
			builder.run();

			return baos.toByteArray();
		}
	}

	// HTML 및 CSS 정제 메서드
	private String sanitizeHtml(String html) {
		return html
				// 불필요한 공백 및 줄바꿈 처리
				.replaceAll("\\s+", " ").replaceAll("\r\n", "\n").replaceAll("\r", "")

				// HTML 엔티티 변환
				.replaceAll("&nbsp;", " ")

				// CSS 속성 정제
				.replaceAll("border-image[^;]*;", "") // border-image 제거
				.replaceAll("border-[^:]+:[^;]+;", "") // border-로 시작하는 CSS 속성 제거
				.replaceAll("initial", "inherit") // initial을 inherit로 대체

				// 지원되지 않는 태그 및 속성 정제
	            .replaceAll("<br>", "<br/>")           // br 태그 수정
	            .replaceAll("<img([^>]*)(?<!/)>", "<img$1 />")  // img 태그 수정
	            .replaceAll("<p[^>]*>", "<p>")         // p 태그 속성 제거
	            .replaceAll("<table[^>]*>", "<table>") // table 태그 속성 제거
	            .replaceAll("<td[^>]*>", "<td>")       // td 태그 속성 제거
	            .replaceAll("<tr[^>]*>", "<tr>");      // tr 태그 속성 제거
	}
}
