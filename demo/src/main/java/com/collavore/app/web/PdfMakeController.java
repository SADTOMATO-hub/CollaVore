package com.collavore.app.web;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.collavore.app.api.service.FlutterApprVO;
import com.collavore.app.api.service.FlutterService;
import com.collavore.app.service.PdfMakeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PdfMakeController {

    private final PdfMakeService pdfMakeService;
    private final FlutterService flutterService;

    @GetMapping("/pdfDownload")
    public ResponseEntity<byte[]> downloadPdf(@RequestParam int empNo, @RequestParam int eaNo) throws IOException {
        FlutterApprVO apprInfo = flutterService.apprInfo(empNo, eaNo);

        if (apprInfo == null || apprInfo.getContent() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("승인 정보를 찾을 수 없습니다.".getBytes(StandardCharsets.UTF_8));
        }

        byte[] pdfBytes = pdfMakeService.createPdfWithHtml(apprInfo.getContent());

        String encodedFileName = URLEncoder.encode(apprInfo.getTitle() + ".pdf", StandardCharsets.UTF_8).replace("+", "%20");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName);
        System.out.println("=====================================================================================================");
        System.out.println(Arrays.toString(pdfBytes));
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}
