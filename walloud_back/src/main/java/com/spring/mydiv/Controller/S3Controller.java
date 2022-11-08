package com.spring.mydiv.Controller;

import com.spring.mydiv.Service.S3UploaderService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller  {
    private final S3UploaderService s3UploaderService;

    @PostMapping("/files/upload")
    public String uploadFile(@RequestPart(value="file",required = false) MultipartFile file) throws IOException {
        return s3UploaderService.upload(file, "test");
//        return new AddDefaultCharsetFilter.ResponseWrapper(new SimpleMessageBody("파일 업로드 성공"));
    }

    @DeleteMapping("/files/delete")
    public String deleteFile(@RequestParam("filename") String filename) {
        System.out.println(filename);
        s3UploaderService.deleteImage(filename);
        return "delete";
    }

    @PostMapping("/files/download")
    public String downloadFile(@RequestParam("imgpath") String imgpath) {
        System.out.println(imgpath);
        String objectURL = s3UploaderService.getFileUrl(imgpath);
        System.out.println(objectURL);
        return objectURL;
    }

}
