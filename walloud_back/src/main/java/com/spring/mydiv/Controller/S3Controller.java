package com.spring.mydiv.Controller;

import com.spring.mydiv.Service.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class S3Controller  {
    private final S3UploaderService s3UploaderService;

    @PostMapping("/files/upload")
    public void uploadFile(@RequestPart(value="file",required = false) MultipartFile file) throws IOException {
        s3UploaderService.upload(file, "test");
//        return new AddDefaultCharsetFilter.ResponseWrapper(new SimpleMessageBody("파일 업로드 성공"));
    }

    @DeleteMapping("/api/images")
    public String deleteFile(@RequestParam("filename") String filename) {
        System.out.println(filename);
        s3UploaderService.deleteImage(filename);
        return "delete";
    }
}
