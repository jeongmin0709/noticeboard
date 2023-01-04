package com.example.noticeboard.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BoardDTO {

    private Long id;

    private String title;

    private String content;

    private String writer;

    private int recomendNum;

    private int viewNum;

    @Builder.Default
    private List<ImageDTO> imageDTOList = new ArrayList<>();

    private LocalDateTime createDate;

    private LocalDateTime modDate;

    private int commentCount;


}
