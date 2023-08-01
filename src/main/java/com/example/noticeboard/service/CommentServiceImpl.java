package com.example.noticeboard.service;

import com.example.noticeboard.dto.CommentDTO;
import com.example.noticeboard.dto.SliceRequestDTO;
import com.example.noticeboard.dto.SliceResultDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.MemberComment;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.notification.NotificationType;
import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.exception.custom_exception.AccessDeniedException;
import com.example.noticeboard.exception.custom_exception.CommentNotFoundException;
import com.example.noticeboard.exception.custom_exception.DuplicateException;
import com.example.noticeboard.repository.MemberCommentRepository;
import com.example.noticeboard.repository.commentRepository.CommentRepository;
import com.example.noticeboard.security.dto.MemberDTO;
import com.example.noticeboard.service.event.NotificationEvent;
import com.example.noticeboard.service.event.RemoveBoardEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {


    private final CommentRepository commentRepository;

    private final MemberCommentRepository memberCommentRepository;

    private final ApplicationEventPublisher publisher;



    //댓글 리스트 출력 함수
    @Override
    @Transactional(readOnly = true)
    public SliceResultDTO<CommentDTO,Map<String, Object>> getCommentList(Long boardId, SliceRequestDTO sliceRequestDTO) {
        Board board = Board.builder().id(boardId).build();
        Function<Map<String, Object>, CommentDTO> fn = (map-> entityToDto((Comment) map.get("comment"), (Long) map.get("childCommentCount")));
        Slice<Map<String, Object>> result = commentRepository.getCommentListByBoard(board, sliceRequestDTO.getPageable());
        return new SliceResultDTO<>(result, fn);
    }

    // 답글 리스트 출력 함수
    @Override
    @Transactional(readOnly = true)
    public SliceResultDTO<CommentDTO, Comment> getChildCommentList(Long commentId, SliceRequestDTO sliceRequestDTO) {
        Comment comment = Comment.builder().id(commentId).build();
        Function<Comment, CommentDTO> fn = (en-> entityToDto(en,0l));
        Slice<Comment> result = commentRepository.getCommentListByParent(comment, sliceRequestDTO.getPageable());
        log.info(result);
        return new SliceResultDTO<>(result, fn);
    }

    @Override
    public Integer recommend(Long id, MemberDTO recommender) {
        Member member = Member.builder().username(recommender.getUsername()).build();

        Optional<Comment> result = commentRepository.findById(id);
        if(result.isEmpty()) throw new CommentNotFoundException(ErrorCode.NOT_FOUND_COMMENT); // 댓글이 존재하지 않는다면
        Comment comment = result.get();

        //자신이 쓴 댓글이라면 추천 x
        if(comment.getMember().getUsername().equals(recommender.getUsername())) throw new AccessDeniedException(ErrorCode.SELF_RECOMMEND);;

        //이미 추천한 댓글이라면 추천 x
        if(memberCommentRepository.existsByMemberAndComment(member, comment)) throw new DuplicateException(ErrorCode.DUPLICATE_RECOMMEND);

        MemberComment memberComment = MemberComment.builder().member(member).comment(comment).build();
        memberCommentRepository.save(memberComment);

        comment.addRecomendNum();

        //알림 이벤트 발행
        publisher.publishEvent(new NotificationEvent(comment, recommender.getUsername(), NotificationType.COMMENT_RECOMMEND));

        return comment.getRecomendNum();
    }

    @Override
    public Long modify(CommentDTO commentDTO, MemberDTO memberDTO) {
        Optional<Comment> result = commentRepository.findById(commentDTO.getId());
        if(result.isEmpty()) throw new CommentNotFoundException(ErrorCode.NOT_FOUND_COMMENT);
        Comment comment = result.get();
        if(!memberDTO.getUsername().equals(comment.getMember().getUsername())) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        comment.changeContent(commentDTO.getContent());
        return comment.getId();
    }

    @Override
    public Long remove(Long id, MemberDTO memberDTO) {
        Optional<Comment> result = commentRepository.findById(id);
        if(result.isEmpty()) throw new CommentNotFoundException(ErrorCode.NOT_FOUND_COMMENT);
        Comment comment = result.get();
        if(!memberDTO.getUsername().equals(comment.getMember().getUsername())) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        memberCommentRepository.deleteByComment(Comment.builder().id(id).build());
        commentRepository.deleteById(id);
        return id;
    }

    @EventListener
    public void RemoveByBoard(RemoveBoardEvent removeBoardEvent){
        commentRepository.deleteByBoard(removeBoardEvent.getBoard());
    }

    @Override
    public CommentDTO register(CommentDTO commentDTO) {

        Comment comment = dtoToEntity(commentDTO);
        commentRepository.save(comment);
        //알림 이벤트 발행
        publisher.publishEvent(new NotificationEvent(comment, commentDTO.getReceiver(), NotificationType.COMMENT_REGISTER));
        return entityToDto(comment, 0l);
    }
}
