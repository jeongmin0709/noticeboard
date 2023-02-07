package com.example.noticeboard.entity;

import lombok.*;

import javax.persistence.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Getter
@Builder
public class Image {

    @Id
    @Column(name = "IMAGE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", nullable = false)
    @ToString.Exclude
    private Board board;

    private String name;

    private String path;

    private String uuid;

    public void setBoard(Board board){
        this.board = board;
    }

    public String getImageURL(){
        try {
            return URLEncoder.encode(path + File.separator + uuid+"_"+ name, "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }
}
