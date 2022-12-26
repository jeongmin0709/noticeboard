package com.example.noticeboard.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardDTO {

    private Long id;

    private String title;

    private String content;

    private String writerNickname;

    private int recomendNum;

    private int viewNum;

    private LocalDateTime createDate;

    private LocalDateTime modDate;

    private int commentCount;

}
