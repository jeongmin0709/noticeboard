package com.example.noticeboard.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentDTO {

    private Long id;

    private Long boardId;

    private Long parentId;

    private Integer childCount;

    private String writer;

    private String content;

    private String receiver;

    private int recommendNum;

    private LocalDateTime createDate;

    private LocalDateTime modDate;

}
