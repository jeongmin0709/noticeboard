package com.example.noticeboard.entity.notification;

import lombok.Getter;

@Getter
public enum NotificationType {
    COMMENT_REGISTER("댓글 등록"),
    BOARD_RECOMMEND("게시글 추천"),
    COMMENT_RECOMMEND("댓글 추천");

    private String description;
    NotificationType(String description) {this.description =description; }
}
