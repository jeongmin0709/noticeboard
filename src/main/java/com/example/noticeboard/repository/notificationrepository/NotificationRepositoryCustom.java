package com.example.noticeboard.repository.notificationrepository;

import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.notification.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface NotificationRepositoryCustom {
    void updateNotificationIsRead(List<Long> idList);
    Slice<Notification> getNotificationListByMember(Member member, Pageable pageable);
}
