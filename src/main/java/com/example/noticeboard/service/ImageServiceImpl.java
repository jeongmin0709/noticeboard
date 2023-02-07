package com.example.noticeboard.service;

import com.example.noticeboard.dto.ImageDTO;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class ImageServiceImpl implements ImageService {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public List<ImageDTO> uploadImg(MultipartFile[] images) throws IOException {
        List<ImageDTO> imageDTOList = new ArrayList<>();
        for (MultipartFile uploadImg : images) {
            // 이미지파일이아니면 리턴
            if(!uploadImg.getContentType().startsWith("image")){
                throw new IllegalArgumentException("이미지 파일이 아닙니다.");
            }
            String originalName = uploadImg.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);
            String folderPath = makeFolder();
            String uuid = UUID.randomUUID().toString();
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);
            log.info("파일 이름: " + fileName);
                //원본파일 저장
            uploadImg.transferTo(savePath);
            //섬네일 생성
            String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" + uuid + "_" + fileName;
            File thumbnailFile = new File(thumbnailSaveName);

            Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 200, 200);
            imageDTOList.add(ImageDTO.builder().fileName(fileName).uuid(uuid).folderPath(folderPath).build());

        }
        return imageDTOList;
    }

    @Override
    public File getImage(String fileName) throws UnsupportedEncodingException {
        String srcFileName = URLDecoder.decode(fileName, "UTF-8");
        File file = new File(uploadPath+File.separator+srcFileName);
        return file;
    }

    @Override
    public boolean removeImg(String ImgName) throws UnsupportedEncodingException {
        String srcImgName = URLDecoder.decode(ImgName, "UTF-8");
        File image = new File(uploadPath+File.separator+srcImgName);
        File thumbnail = new File(image.getParent(), "s_" +image.getName());
        return image.delete() && thumbnail.delete();
    }


    private String makeFolder(){
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);
        File uploadPathFolder = new File(uploadPath, folderPath);
        if(!uploadPathFolder.exists()){
            log.info("폴더 경로: "+uploadPathFolder);
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

}
