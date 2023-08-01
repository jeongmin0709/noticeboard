package com.example.noticeboard.repository;

import com.example.noticeboard.config.JpaConfig;
import com.example.noticeboard.config.SecurityConfig;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.notification.Notification;
import com.example.noticeboard.entity.notification.NotificationType;
import com.example.noticeboard.repository.notificationrepository.NotificationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@DataJpaTest
@Import(JpaConfig.class)
public class NotificationRepositoryTests {

    private PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void saveMember(){
        Member member = Member.builder()
                .username("Member")
                .email("Member"+"@sample.org")
                .name("name")
                .password(passwordEncoder.encode("1111"))
                .fromSocial(false)
                .build();
        memberRepository.save(member);
    }
    @Test
    public void updateNotificationIsRead(){
        //given
        List<Long> idList = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i->{
            Notification notification = Notification.builder()
                    .content("content..."+i)
                    .notificationType(NotificationType.BOARD_RECOMMEND)
                    .isRead(false)
                    .url("/test/"+i)
                    .member(Member.builder().username("Member").build())
                    .build();
            Notification save = notificationRepository.save(notification);
            idList.add(save.getId());
        });
        //when, then
        notificationRepository.updateNotificationIsRead(idList);
    }
}
