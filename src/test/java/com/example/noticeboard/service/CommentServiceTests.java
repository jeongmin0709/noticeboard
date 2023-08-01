package com.example.noticeboard.service;

import com.example.noticeboard.dto.CommentDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.dto.SliceRequestDTO;
import com.example.noticeboard.dto.SliceResultDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.exception.custom_exception.AccessDeniedException;
import com.example.noticeboard.exception.custom_exception.DuplicateException;
import com.example.noticeboard.exception.custom_exception.NotFoundException;
import com.example.noticeboard.repository.MemberCommentRepository;
import com.example.noticeboard.repository.commentRepository.CommentRepository;
import com.example.noticeboard.security.dto.MemberDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.SliceImpl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("댓글 서비스 테스트")
public class CommentServiceTests {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberCommentRepository memberCommentRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    CommentServiceImpl commentService;

    @Test
    @DisplayName("댓글 리스트를 가져온다.")
    public void getCommentList(){
        //given
        Long boardId = 1l;
        List<Map<String, Object>> list = IntStream.rangeClosed(1, 10).boxed().map(i -> {
            Map<String, Object> map = new HashMap<>();
            Member member =Member.builder().username("user..."+i).build();
            Board board = Board.builder().id(1l).build();
            Comment comment = Comment.builder().id(i.longValue()).member(member).board(board).content("content..."+i).build();
            map.put("comment", comment);
            map.put("childCommentCount", i.longValue());
            return map;
        }).collect(Collectors.toList());
        SliceRequestDTO sliceRequestDTO= SliceRequestDTO.builder().page(1).size(10).build();
        when(commentRepository.getCommentListByBoard(any(), any()))
                .thenReturn(new SliceImpl<>(list, sliceRequestDTO.getPageable(), true));
        //when
        SliceResultDTO<CommentDTO, Map<String, Object>> commentList = commentService.getCommentList(boardId, sliceRequestDTO);
        //then
        Assertions.assertThat(commentList.isHasNext()).isTrue();
        Assertions.assertThat(commentList.getSize()).isEqualTo(10);
        for(Integer i=1; i<=10; i++) {
            CommentDTO commentDTO = commentList.getDtoList().get(i-1);
            Assertions.assertThat(commentDTO.getId()).isEqualTo(i.longValue());
            Assertions.assertThat(commentDTO.getBoardId()).isEqualTo(1l);
            Assertions.assertThat(commentDTO.getWriter()).isEqualTo("user..."+i);
            Assertions.assertThat(commentDTO.getContent()).isEqualTo("content..."+i);
        }
    }
    @Test
    @DisplayName("답글 리스트를 가져온다.")
    public void getChildCommentList(){
        //given
        Long boardId = 1l;
        Long parentId = 1l;
        List<Comment> list = IntStream.rangeClosed(2, 11).boxed().map(i -> {
            Comment childComment = Comment.builder()
                    .id(i.longValue())
                    .parent(Comment.builder().id(parentId).build())
                    .member(Member.builder().username("user..." + i).build())
                    .board(Board.builder().id(1l).build())
                    .content("content..." + i)
                    .build();
            return childComment;
        }).collect(Collectors.toList());
        SliceRequestDTO sliceRequestDTO= SliceRequestDTO.builder().page(1).size(10).build();
        when(commentRepository.getCommentListByParent(any(), any()))
                .thenReturn(new SliceImpl<>(list, sliceRequestDTO.getPageable(), true));
        //when
        SliceResultDTO<CommentDTO, Comment> childCommentList = commentService.getChildCommentList(parentId, sliceRequestDTO);
        //then
        Assertions.assertThat(childCommentList.isHasNext()).isTrue();
        Assertions.assertThat(childCommentList.getSize()).isEqualTo(10);
        for(Integer i=2; i<=10; i++) {
            CommentDTO commentDTO = childCommentList.getDtoList().get(i-2);
            Assertions.assertThat(commentDTO.getId()).isEqualTo(i.longValue());
            Assertions.assertThat(commentDTO.getBoardId()).isEqualTo(boardId);
            Assertions.assertThat(commentDTO.getParentId()).isEqualTo(parentId);
            Assertions.assertThat(commentDTO.getWriter()).isEqualTo("user..."+i);
            Assertions.assertThat(commentDTO.getContent()).isEqualTo("content..."+i);
        }
    }

    @Nested
    @DisplayName("댓글을 추천한다.")
    public class recommendCommentTest{
        @Test
        @DisplayName("존재하지 않는 댓글을 추천한다.")
        public void notFountBoardRecommendTest(){
            //given
            Long id = 1l;
            MemberDTO memberDTO = MemberDTO.builder().build();
            when(commentRepository.findById(1l)).thenReturn(Optional.empty());
            //when, then
            Assertions.assertThatThrownBy(()->commentService.recommend(id, memberDTO)).isInstanceOf(NotFoundException.class);
        }
        @Test
        @DisplayName("자신이 쓴 댓글을 추천한다.")
        public void selfRecommendBoardTest(){
            //given
            Long id = 1l;
            MemberDTO memberDTO = MemberDTO.builder().username("testUsername").build();
            when(commentRepository.findById(1l)).thenReturn(Optional.of(Comment.builder().member(Member.builder().username("testUsername").build()).build()));
            //when,then
            Assertions.assertThatThrownBy(()->commentService.recommend(id, memberDTO)).isInstanceOf(AccessDeniedException.class);
        }
        @Test
        @DisplayName("이미 추천한 댓글을 추천한다.")
        public void duplicateRecommendBoardTest(){
            //given
            Long id = 1l;
            MemberDTO memberDTO = MemberDTO.builder().username("testUsername").build();
            Optional<Comment> optional = Optional.of(Comment.builder().id(1l).member(Member.builder().username("diffUsername").build()).build());
            when(commentRepository.findById(1l)).thenReturn(optional);
            doReturn(true).when(memberCommentRepository).existsByMemberAndComment(any(Member.class),any(Comment.class));
            //when, then
            Assertions.assertThatThrownBy(()->commentService.recommend(id, memberDTO)).isInstanceOf(DuplicateException.class);
        }
        @Test
        @DisplayName("일반적인 댓글을 추천한다.")
        public void normalRecommendBoardTest(){
            //given
            Long id = 1l;
            MemberDTO memberDTO = MemberDTO.builder().username("testUsername").build();
            Optional<Comment> optional = Optional.of(Comment.builder()
                    .id(1l)
                    .recomendNum(5)
                    .member(Member.builder().username("diffUsername").build())
                    .build());
            when(commentRepository.findById(1l)).thenReturn(optional);
            doReturn(false).when(memberCommentRepository).existsByMemberAndComment(any(Member.class),any(Comment.class));
            //when
            Integer recommendNum = commentService.recommend(id, memberDTO);
            //then
            Assertions.assertThat(recommendNum).isEqualTo(6);
        }
    }
}
