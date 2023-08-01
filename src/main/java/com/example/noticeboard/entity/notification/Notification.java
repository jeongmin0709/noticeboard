package com.example.noticeboard.entity.notification;

import com.example.noticeboard.entity.BaseEntity;
import com.example.noticeboard.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Notification extends BaseEntity {

    @Id
    @Column(name = "notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id", nullable = false)
    @ToString.Exclude
    private Member member;

    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String url;

    private boolean isRead;

    public void setRead(boolean read) {
        isRead = read;
    }
}
