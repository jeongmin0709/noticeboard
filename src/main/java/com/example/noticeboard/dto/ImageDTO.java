package com.example.noticeboard.dto;

import lombok.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ImageDTO {

    private Long boardId;

    private String uuid;

    private String fileName;

    private String folderPath;

    public String getImageURL(){
        try {
            return URLEncoder.encode(folderPath + File.separator + uuid+"_"+ fileName, "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL(){
        try {
            return URLEncoder.encode(folderPath + File.separator +"s_"+uuid+"_"+ fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
