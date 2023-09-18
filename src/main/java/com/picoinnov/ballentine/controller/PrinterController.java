package com.picoinnov.ballentine.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.picoinnov.ballentine.service.PrinterService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class PrinterController {
	
	@Autowired
	PrinterService service;
	
	private Logger logger = LoggerFactory.getLogger(PrinterController.class);
	
	@PostMapping("/print.do")
	public void print(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, Object> map, @RequestParam MultipartFile pdfBytes) throws IOException {
		
		byte[] arrByte = pdfBytes.getBytes();					// 문서 byte 배열
		String printer = (String)map.get("printer");			// 프린터 명칭
		String orientation = (String)map.get("orientation");	// 프린터 명칭
			
		PrintWriter out = response.getWriter();
        
		try {
			service.print(arrByte, printer, orientation); 			// 인쇄실행
			
			response.setStatus(HttpServletResponse.SC_OK);	// 인쇄성공 응답 설정	
			out.println("PRINT SUCCESS : " + printer);			
			logger.info("PRINT SUCCESS : " + printer);
		} catch (Exception e) {			
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			out.println(e.getMessage());
			logger.error(e.getMessage(), e.fillInStackTrace());
		} finally {	
			// CORS 정책 허용 헤더 설정
			response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
			response.setHeader("Content-Type", "application/json");
			response.setHeader("Accept", "application/json");
			
			logger.info("return status : " + response.getStatus() + ", Origin : " + request.getHeader("Origin"));
			
			out.flush();
			out.close();	// response 스트림 close
		}
		
		// 테스트용 파일 저장. 필요없는 경우 삭제
//		FileOutputStream fos = new FileOutputStream("C:/app/Ballentine/report_temp.pdf");
//		BufferedOutputStream bos = new BufferedOutputStream(fos);
//		bos.write(arrByte);		
//		bos.close();
		
		return;
	}
}

