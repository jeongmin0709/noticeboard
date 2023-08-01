package com.example.noticeboard.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class NotificationDTO {

    private Long id;

    private String receiver;

    private String content;

    private String url;

    private String type;

    private boolean read;

    private LocalDateTime createDate;

}
