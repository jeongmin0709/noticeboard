package com.example.noticeboard.service;

import com.example.noticeboard.dto.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ImageService {

    List<ImageDTO> uploadImg(MultipartFile[] multipartFiles);
    File getImage(String fileName, String size) throws IOException;
    boolean removeImg(String ImgName) throws IOException;
}
