package com.example.noticeboard.service;

import com.example.noticeboard.dto.CommentDTO;
import com.example.noticeboard.dto.NotificationDTO;
import com.example.noticeboard.dto.SliceRequestDTO;
import com.example.noticeboard.dto.SliceResultDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.notification.Notification;
import com.example.noticeboard.entity.notification.NotificationType;
import com.example.noticeboard.repository.EmitterRepository;
import com.example.noticeboard.repository.notificationrepository.NotificationRepository;
import com.example.noticeboard.service.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService{

    private final NotificationRepository notificationRepository;

    private final EmitterRepository emitterRepository;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 30;

    @Override
    // SSE(server send message) 구독
    public SseEmitter subscribe(String username) {
        String id = username +"_" +System.currentTimeMillis();
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(id, emitter);

        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
        emitter.onTimeout(() -> emitterRepository.deleteById(id));
        emitter.onError((e) -> emitterRepository.deleteById(id));

        SliceRequestDTO sliceRequestDTO = SliceRequestDTO.builder().page(1).size(20).build();
        SliceResultDTO<NotificationDTO, Notification> notificationList = getNotificationList(sliceRequestDTO, username);
        sendToClient(id, "connect" , notificationList);
        return emitter;
    }
    @Override
    // 알림 리스트
    public SliceResultDTO<NotificationDTO, Notification> getNotificationList(SliceRequestDTO sliceRequestDTO, String username){
        Member member = Member.builder().username(username).build();
        Slice<Notification> result = notificationRepository.getNotificationListByMember(member, sliceRequestDTO.getPageable());
        Function<Notification, NotificationDTO> fn = (en-> entityToDto(en));
        return new SliceResultDTO<>(result, fn);
    }

    @Override
    public void updateNotificationIsRead(List<Long> idList){
        notificationRepository.updateNotificationIsRead(idList);
    }

    private void sendToClient(String username, String eventName,Object data){
        Map<String, SseEmitter> emitters = emitterRepository.findAllStartWithByUsername(username);
        emitters.forEach((id, emitter) -> {
                    try {
                        emitter.send(SseEmitter.event().id(id).name(eventName).data(data));

                    } catch (IOException exception) {
                        emitterRepository.deleteById(id);
                    }
                }
        );
    }

    @Override
    @EventListener
    public void notificationEventListener(NotificationEvent notificationEvent){
        Object entity = notificationEvent.getEntity();
        String username = notificationEvent.getUsername();
        NotificationType type = notificationEvent.getType();
        Notification notification;
        // notification entity 생성
        if(type == NotificationType.BOARD_RECOMMEND) notification= makeBoardRecommendNotification((Board) entity, username);
        else if(type == NotificationType.COMMENT_RECOMMEND) notification = makeCommentRecommendNotification((Comment) entity, username);
        else notification = makeCommentRegisterNotification((Comment) entity, username);
        Notification save = notificationRepository.save(notification);
        sendToClient(notification.getMember().getUsername(), "message",entityToDto(save));
    }

    private Notification makeBoardRecommendNotification(Board board, String recommender){
        String title = board.getTitle();
        if(title.length() > 30) title = title.substring(0, 20) + "...";
        return  Notification.builder()
                .member(board.getMember())
                .url("/read/" + board.getId())
                .content(recommender+"님이 \""+ title +"\" 게시글을 추천하였습니다.")
                .notificationType(NotificationType.BOARD_RECOMMEND)
                .build();
    }

    private Notification makeCommentRecommendNotification(Comment comment, String recommender){
        String content = comment.getContent();
        if(content.length() > 30) content = content.substring(0, 20) + "...";
        //알림 이벤트 발행
        return Notification.builder()
                .member(comment.getMember())
                .url("/read/" + comment.getBoard().getId())
                .content(recommender+"님이 \""+ content +"\" 댓글을 추천하였습니다.")
                .notificationType(NotificationType.COMMENT_RECOMMEND)
                .build();
    }

    private Notification makeCommentRegisterNotification(Comment comment, String receiver){
        log.info(receiver);
        String content = comment.getContent();
        if(content.length() > 30) content = content.substring(0, 30) + "...";
        Notification notification;
        if(comment.getParent() == null){
            notification = Notification.builder()
                    .member(Member.builder().username(receiver).build()) // 쿼리 실행
                    .url("/read/"+ comment.getBoard().getId())
                    .content(comment.getMember().getUsername()+"님아 댓글 \"" + content+"\"을 등록하였습니다." )
                    .notificationType(NotificationType.COMMENT_REGISTER)
                    .build();
        }else {
            notification = Notification.builder()
                    .member(Member.builder().username(receiver).build()) // 쿼리 실행
                    .url("/read/" + comment.getBoard().getId())
                    .content(comment.getMember().getUsername() + "님이 답글 \"" + content + "\"을 등록하였습니다.")
                    .notificationType(NotificationType.COMMENT_REGISTER)
                    .build();
        }
        return notification;
    }

}
