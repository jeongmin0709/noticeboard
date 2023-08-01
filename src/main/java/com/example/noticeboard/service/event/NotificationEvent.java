package com.example.noticeboard.service.event;

import com.example.noticeboard.entity.notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotificationEvent<EN> {

    private EN entity;

    private String username;

    private NotificationType type;
}
