package com.example.noticeboard.controller;

import com.example.noticeboard.dto.ImageDTO;
import com.example.noticeboard.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/image")
public class ImageRestController {

    private final ImageService imageService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public List<ImageDTO> uploadImage(MultipartFile[] images) throws IOException {
        log.info("이미지 업로드 요청");
        return imageService.uploadImg(images);
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<byte[]> getImage(String fileName) throws IOException{
        log.info("{} 이미지 요청", fileName);
        File image = imageService.getImage(fileName);
        //MIME 타입 처리
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Type", Files.probeContentType(image.toPath()));
        return new ResponseEntity<>(FileCopyUtils.copyToByteArray(image), header, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping
    Boolean removeImage(String fileName) throws IOException{
        log.info("{} 이미지 삭제 요청", fileName);
        return imageService.removeImg(fileName);
    }
}
