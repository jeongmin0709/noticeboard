package com.example.noticeboard.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
public class PagingBoardDTO {

    private Long id;

    private String title;

    private String writer;

    private int recomendNum;

    private int viewNum;

    boolean hasImage;

    private LocalDateTime createDate;

    private int commentCount;

    @Builder
    @QueryProjection
    public PagingBoardDTO(Long id, String title, String writer, int recomendNum, int viewNum, LocalDateTime createDate, boolean hasImage, Long commentCount) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.recomendNum = recomendNum;
        this.viewNum = viewNum;
        this.hasImage = hasImage;
        this.createDate = createDate;
        this.commentCount = commentCount.intValue();
    }
}
