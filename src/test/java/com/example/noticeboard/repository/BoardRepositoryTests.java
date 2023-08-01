package com.example.noticeboard.repository;

import com.example.noticeboard.DummyDataProvider;
import com.example.noticeboard.config.JpaConfig;
import com.example.noticeboard.dto.PagingBoardDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.Image;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.repository.boardrepository.BoardRepository;
import com.example.noticeboard.repository.commentRepository.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


@DataJpaTest
@Import({JpaConfig.class, DummyDataProvider.class})
@DisplayName("게시글 저장소 테스트")
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;
    @Test
    @DisplayName("게시글 저장")
    public void saveBoard(){
        //given
        Board board = Board.builder()
                .title("saveTest")
                .member(Member.builder().username("user1").build())
                .content("content")
                .build();
        //when
        Board save = boardRepository.save(board);
        //then
        Assertions.assertThat(save).isSameAs(save);
    }


    @Test
    @DisplayName("저장된 게시글과 댓글수를 조회")
    public void getBoardTest(){
        //given
        Long boardId = 1l;

        //when
        Map<String, Object> result = boardRepository.getBoard(boardId);
        Board findBoard = (Board) result.get("board");
        Long commentCount = (Long) result.get("commentCount");
        //then
        Assertions.assertThat(boardId).isEqualTo(findBoard.getId());
        Assertions.assertThat(findBoard.getTitle()).isEqualTo("title...1");
        Assertions.assertThat(commentCount).isEqualTo(110);
    }

    @Test
    @DisplayName("저장된 게시글을 조회")
    public void getBoardWithImageTest(){
        //given
        Long boardId =10L;
        //when
        Optional<Board> result = boardRepository.getBoardWithImage(boardId);
        Board findBoard = result.get();
        //then
        Assertions.assertThat(findBoard.getId()).isEqualTo(boardId);
        Assertions.assertThat(findBoard.getTitle()).isEqualTo("title...10");
        Assertions.assertThat(findBoard.getImageList().size()).isEqualTo(6);
    }


    @DisplayName("게시글 리스트를 조회")
    @Nested
    public class getBoardListTest{
        //given
        private Pageable pageable = PageRequest.of(0, 20, Sort.by("id").descending());
        @Test
        @DisplayName("일반적인 게시글 리스트 조회")
        public void getNormalBoardListTest(){
            //when
            Page<PagingBoardDTO> result = boardRepository.getBoardPage(null, null,null , null, pageable);
            Pageable findPageable = result.getPageable();
            //then
            Assertions.assertThat(result.getTotalElements()).isEqualTo(100);
            Assertions.assertThat(result.getTotalPages()).isEqualTo(5);
            Assertions.assertThat(findPageable.getPageNumber()).isEqualTo(0);
            Assertions.assertThat(findPageable.getPageSize()).isEqualTo(20);

        }
        @Test
        @DisplayName("제목으로 게시글 리스트 조회")
        public void getBoardListByTitleTest(){
            //when
            Page<PagingBoardDTO> result = boardRepository.getBoardPage("t", "title...22",null , null, pageable);
            //then
            Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
            result.stream().forEach(pagingBoardDTO -> Assertions.assertThat(pagingBoardDTO.getTitle()).contains("title...22"));
        }

        @Test
        @DisplayName("내용으로 게시글 리스트 조회")
        public void getBoardListByContentTest(){
            //when
            Page<PagingBoardDTO> result = boardRepository.getBoardPage("c", "3",null , null, pageable);
            //then
            Assertions.assertThat(result.getTotalElements()).isEqualTo(19);
        }
        @Test
        @DisplayName("글쓴이로 게시글 리스트 조회")
        public void getBoardListByWriterTest(){
            //when
            Page<PagingBoardDTO> result = boardRepository.getBoardPage("w", "er2",null , null, pageable);
            //then
            Assertions.assertThat(result.getTotalElements()).isEqualTo(10);
            result.stream().forEach(pagingBoardDTO -> Assertions.assertThat(pagingBoardDTO.getWriter()).contains("er2"));
        }
        @Test
        @DisplayName("제목과 내용으로 리스트 조회")
        public void getBoardListByTitleAndContentTest(){
            //when
            Page<PagingBoardDTO> result = boardRepository.getBoardPage("tc", "le...1",null , null, pageable);
            //then
            Assertions.assertThat(result.getTotalElements()).isEqualTo(12);
            result.stream().forEach(pagingBoardDTO -> { Assertions.assertThat(pagingBoardDTO.getTitle()).contains("le...1"); });
        }
        @Test
        @DisplayName("제목과 내용과 글쓴이로 리스트 조회")
        public void getBoardListByTitleAndContentAndWriterTest(){
            //when
            Page<PagingBoardDTO> result = boardRepository.getBoardPage("tcw", "..2",null , null, pageable);
            //then
            Assertions.assertThat(result.getTotalElements()).isEqualTo(11);
            result.stream().forEach(pagingBoardDTO -> {
                Assertions.assertThat(pagingBoardDTO.getTitle()).contains("..2");
                Assertions.assertThat(pagingBoardDTO.getWriter()).doesNotContain("..2");
            });
        }
        @Test
        @DisplayName("자신의 게시글 리스트 조회")
        public void getMyBoardListTest(){
            //given
            Member member = Member.builder().username("user1").build();
            //when
            Page<PagingBoardDTO> result = boardRepository.getBoardPage(null, null,"board" , member.getUsername(), pageable);
            //then
            Assertions.assertThat(result.getTotalElements()).isEqualTo(10);
            result.stream().forEach(pagingBoardDTO -> {
                Assertions.assertThat(pagingBoardDTO.getWriter()).isEqualTo(member.getUsername());
            });
        }
        @Test
        @DisplayName("자신의 댓글이 달린 게시글 리스트 조회")
        public void getBoardListByMyCommentTest(){
            //given
            Member member = Member.builder().username("user1").build();
            //when
            Page<PagingBoardDTO> result = boardRepository.getBoardPage(null, null,"comment" , member.getUsername(), pageable);
            //then
            Assertions.assertThat(result.getTotalElements()).isEqualTo(10);
        }
    }
    @Nested
    @DisplayName("이전글 및 다음글을 조회")
    public class getPrevAndNextBoardTest{
        @Test
        @DisplayName("시작글의 이전글 및 다음글을 조회")
        public void getPrevAndNextStartBoardTest(){
            //given
            Long id = 1l;
            //when
            Map<String, Board> prevAndNextBoard = boardRepository.getPrevAndNextBoard(id);
            //then
            Assertions.assertThat(prevAndNextBoard.get("prev")).isNull();
            Assertions.assertThat(prevAndNextBoard.get("next")).isNotNull();
        }
        @Test
        @DisplayName("중간글의 이전글 및 다음글을 조회")
        public void getPrevAndNextMiddleBoardTest(){
            //given
            Long id = 50l;
            //when
            Map<String, Board> prevAndNextBoard = boardRepository.getPrevAndNextBoard(id);
            //then
            Assertions.assertThat(prevAndNextBoard.get("prev")).isNotNull();
            Assertions.assertThat(prevAndNextBoard.get("next")).isNotNull();
        }
        @Test
        @DisplayName("마지막글의 이전글 및 다음글을 조회")
        public void getPrevAndNextEndBoardTest(){
            //given
            Long id = 100l;
            //when
            Map<String, Board> prevAndNextBoard = boardRepository.getPrevAndNextBoard(id);
            //then
            Assertions.assertThat(prevAndNextBoard.get("prev")).isNotNull();
            Assertions.assertThat(prevAndNextBoard.get("next")).isNull();
        }
    }

}
