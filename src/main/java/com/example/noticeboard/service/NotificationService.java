package com.example.noticeboard.service;

import com.example.noticeboard.dto.*;
import com.example.noticeboard.entity.notification.Notification;
import com.example.noticeboard.service.event.NotificationEvent;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;;import java.util.List;

public interface NotificationService {

    SseEmitter subscribe(String username);
    void notificationEventListener(NotificationEvent notificationEvent);
    void updateNotificationIsRead(List<Long> idList);
    SliceResultDTO<NotificationDTO, Notification> getNotificationList(SliceRequestDTO sliceRequestDTO, String username);

    default NotificationDTO entityToDto(Notification notification){
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .id(notification.getId())
                .receiver(notification.getMember().getUsername())
                .content(notification.getContent())
                .url(notification.getUrl())
                .read(notification.isRead())
                .type(notification.getNotificationType().getDescription())
                .createDate(notification.getCreateDate())
                .build();

        return notificationDTO;
    }
}
