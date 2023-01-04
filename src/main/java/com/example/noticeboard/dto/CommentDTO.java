package com.example.noticeboard.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentDTO {

    private Long id;

    private Long boardId;

    private String writer;

    private String content;

    private int recomendNum;

    private LocalDateTime createDate;

    private LocalDateTime modDate;

}
