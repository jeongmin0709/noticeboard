package com.example.noticeboard.service;

import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.notification.NotificationType;
import com.example.noticeboard.service.event.NotificationEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@SpringBootTest
public class NotificationServiceTests {

    @Autowired
    NotificationService notificationService;

    @Test
    @DisplayName("알림 구독을 진행한다.")
    void subscribeTest(){
        //give
        String username = "tjrwjdals999";
        //when
        SseEmitter emitter = notificationService.subscribe(username);
        //then
        Assertions.assertThat(emitter).isNotNull();
    }
    @Test
    @DisplayName("알림이 발생하면 알림 이벤트 리스너가 알림을 전송한다")
    void sendMessageTest(){
        //given
        Board board = Board.builder().id(100000l)
                .title("test").member(Member.builder().username("testReceiver").build()).build();
        NotificationEvent<Board> notificationEvent = new NotificationEvent<>(board, "testSender", NotificationType.BOARD_RECOMMEND);
        //when
        notificationService.notificationEventListener(notificationEvent);
        //then
    }
}
