package com.winnie.upload.controller;

import com.winnie.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class UploadController {
    @Autowired
    UploadService uploadService;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImg(@RequestParam("file") MultipartFile file){
        String url = uploadService.uploadImg(file);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/signature")
    public ResponseEntity<Map<String,Object>> getSignature(){
        Map<String,Object> signature = uploadService.getSignature();
        return ResponseEntity.ok(signature);
    }
}
