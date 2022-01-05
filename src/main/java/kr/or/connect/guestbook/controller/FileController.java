package kr.or.connect.guestbook.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class FileController {
	
	@GetMapping("/uploadform")
	public String uploadform() {
		return "uploadform";
	}
	
	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file) {
		
		System.out.println("파일 이름 : " + file.getOriginalFilename());
		System.out.println("파일 크기 : " + file.getSize());
		
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("/tmp/" + file.getOriginalFilename());
			InputStream inputStream = file.getInputStream();
		
			int readCount = 0;
			byte[] buffer = new byte[1024];
			
			while ((readCount = inputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, readCount);
			}
		}
		catch(Exception e) {
			throw new RuntimeException("File Save Error");
		}
		
		return "uploadok";
	}
	
	@GetMapping("/download")
	public void download(HttpServletResponse response) {
		
		String fileName ="otters.jpg";
		String saveFileName = "/tmp/otters.jpg";
		String contentType = "image/jpg";
		int fileLength = 139329;
		
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", contentType);
		response.setHeader("Content-Length", "" + fileLength);
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expire", "-1;");
		
		try {
			FileInputStream fileInputStream = new FileInputStream(saveFileName);
			OutputStream outputStream = response.getOutputStream();
		
			int readCount = 0;
			byte[] buffer = new byte[1024];
			
			while ((readCount = fileInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readCount);
			}
		}
		catch(Exception e) {
			throw new RuntimeException("File Save Error");
		}
	}
}
