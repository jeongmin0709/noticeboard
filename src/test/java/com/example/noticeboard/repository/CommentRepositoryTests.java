package com.example.noticeboard.repository;

import com.example.noticeboard.DummyDataProvider;
import com.example.noticeboard.config.JpaConfig;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.repository.boardrepository.BoardRepository;
import com.example.noticeboard.repository.commentRepository.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@DataJpaTest
@Import({JpaConfig.class, DummyDataProvider.class})
@DisplayName("댓글 저장소 테스트")
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 등혹")
    public void saveCommentTest(){
        //given
        Board board = Board.builder().id(1l).build();
        Member member = Member.builder().username("user1").build();
        Comment comment = Comment.builder()
                .board(board)
                .content("content")
                .member(member)
                .build();
        //when
        Comment saveComment = commentRepository.save(comment);
        //then
        Assertions.assertThat(comment).isSameAs(saveComment);
        Assertions.assertThat(comment.getId()).isEqualTo(saveComment.getId());
        Assertions.assertThat(comment.getContent()).isEqualTo(saveComment.getContent());
    }
    @Test
    @DisplayName("답글 등록")
    public void saveChildCommentTest(){
        //given
        Board board = Board.builder().id(1l).build();
        Member member = Member.builder().username("user1").build();
        Comment comment = Comment.builder().id(1l).build();
        //when
        Comment childComment = Comment.builder()
                .parent(comment)
                .board(board)
                .content("content")
                .member(member)
                .build();
        Comment saveChildComment = commentRepository.save(childComment);
        //then
        Assertions.assertThat(saveChildComment).isSameAs(childComment);
        Assertions.assertThat(saveChildComment.getParent()).isEqualTo(comment);
    }
    @Test
    @DisplayName("댓글 리스트 조회")
    public void getListByBoardTest(){
        //given
        Board board = Board.builder().id(1l).build();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        //when
        Slice<Map<String, Object>> commentList = commentRepository.getCommentListByBoard(board, pageable);
        //then
        Assertions.assertThat(commentList.getSize()).isEqualTo(10);
        commentList.stream().forEach(map -> {
            Comment findComment = (Comment) map.get("comment");
            Long ChildCommentNum = (Long) map.get("childCommentCount");

            Assertions.assertThat(ChildCommentNum).isEqualTo(10l);
            Assertions.assertThat(findComment.getBoard().getId()).isEqualTo(board.getId());
            Assertions.assertThat(findComment.getParent()).isNull();
        });

    }

    @Test
    @DisplayName("답급 리스트 조회")
    public void getListByComment(){
        //given
        Comment comment = Comment.builder().id(1l).build();
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        //when
        Slice<Comment> childCommentSlice = commentRepository.getCommentListByParent(comment, pageable);
        //then
        Assertions.assertThat(childCommentSlice.getSize()).isEqualTo(10);
        childCommentSlice.stream().forEach(childComment->{
            Assertions.assertThat(childComment.getParent().getId()).isEqualTo(comment.getId());
        });
    }

}
