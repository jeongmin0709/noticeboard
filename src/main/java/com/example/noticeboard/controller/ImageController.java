package com.example.noticeboard.controller;

import com.example.noticeboard.dto.ImageDTO;
import com.example.noticeboard.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ImageController {

    private final ImageService imageService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/upload")
    public ResponseEntity<List<ImageDTO>> uploadImg(MultipartFile[] uploadImgs){
        log.info("이미지 업로드 요청");
        try {
            List<ImageDTO> imageDTOList = imageService.uploadImg(uploadImgs);
            return  new ResponseEntity<>(imageDTOList, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/display")
    public ResponseEntity<byte[]> getImg(String fileName, String size){
        log.info("이미지 데이터 요청");
        try {
            File image = imageService.getImage(fileName, size);
            //MIME 타입 처리
            HttpHeaders header = new HttpHeaders();
            header.add("Content-Type", Files.probeContentType(image.toPath()));
            return new ResponseEntity<>(FileCopyUtils.copyToByteArray(image), header, HttpStatus.OK);

        }catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/removeImg")
    ResponseEntity<Boolean> removeImg(String imgName){
        log.info("이미지 삭제 요청: ");
        try {
            boolean result = imageService.removeImg(imgName);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
