package com.example.noticeboard.service;

import com.example.noticeboard.dto.BoardDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.dto.PageResultDTO;
import com.example.noticeboard.dto.PagingBoardDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.exception.custom_exception.DuplicateException;
import com.example.noticeboard.exception.custom_exception.NotFoundException;
import com.example.noticeboard.repository.MemberBoardRepository;
import com.example.noticeboard.repository.boardrepository.BoardRepository;
import com.example.noticeboard.security.dto.MemberDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("게시글 서비스 테스트")
public class BoardServiceTests {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private MemberBoardRepository memberBoardRepository;
    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private BoardServiceImpl boardServiceImpl;

    @Nested
    @DisplayName("게시글 페이지를 가져온다")
    public class getBoardListTest{
        private int total = 500;
        private int size = 20;

        @Test
        @DisplayName("시작 페이지를 가져온다")
        void startPageTest(){
            //given
            PageRequestDTO pageRequestDTO= PageRequestDTO.builder().page(1).size(size).build();
            when(boardRepository.getBoardPage(null, null, null, null, pageRequestDTO.getPageable()))
                    .thenReturn(new PageImpl<>(new ArrayList<>(), pageRequestDTO.getPageable(), total));
            //when
            PageResultDTO<PagingBoardDTO, Board> list = boardServiceImpl.getList(pageRequestDTO, null);
            //then
            Assertions.assertThat(list.getStart()).isEqualTo(1);
            Assertions.assertThat(list.getEnd()).isEqualTo(10);
            Assertions.assertThat(list.getTotalPage()).isEqualTo(25);
            Assertions.assertThat(list.isPrev()).isFalse();
            Assertions.assertThat(list.isNext()).isTrue();
        }
        @Test
        @DisplayName("중간 페이지를 가져온다")
        public void middlePageTest(){
            //given
            PageRequestDTO pageRequestDTO= PageRequestDTO.builder().page(15).size(size).build();
            when(boardRepository.getBoardPage(null, null, null, null, pageRequestDTO.getPageable()))
                    .thenReturn(new PageImpl<>(new ArrayList<>(), pageRequestDTO.getPageable(), total));
            //when
            PageResultDTO<PagingBoardDTO, Board> list = boardServiceImpl.getList(pageRequestDTO, null);
            //then
            Assertions.assertThat(list.getStart()).isEqualTo(11);
            Assertions.assertThat(list.getEnd()).isEqualTo(20);
            Assertions.assertThat(list.getTotalPage()).isEqualTo(25);
            Assertions.assertThat(list.isPrev()).isTrue();
            Assertions.assertThat(list.isNext()).isTrue();
        }
        @Test
        @DisplayName("끝 페이지를 가져온다")
        void endPageTest(){
            //given
            PageRequestDTO pageRequestDTO= PageRequestDTO.builder().page(25).size(size).build();
            when(boardRepository.getBoardPage(null, null, null, null, pageRequestDTO.getPageable()))
                    .thenReturn(new PageImpl<>(new ArrayList<>(), pageRequestDTO.getPageable(), total));
            //when
            PageResultDTO<PagingBoardDTO, Board> list = boardServiceImpl.getList(pageRequestDTO, null);
            //then
            Assertions.assertThat(list.getStart()).isEqualTo(21);
            Assertions.assertThat(list.getEnd()).isEqualTo(25);
            Assertions.assertThat(list.getTotalPage()).isEqualTo(25);
            Assertions.assertThat(list.isPrev()).isTrue();
            Assertions.assertThat(list.isNext()).isFalse();
        }
    }

    @Nested
    @DisplayName("게시글을 가져온다.")
    public class getBoardTest{
        @DisplayName("존재하지 않는 게시글을 가져온다.")
        @Test
        public void getNotFoundBoardTest(){
            //given
            Long id = 100l;
            when(boardRepository.getBoard(id))
                    .thenReturn(new HashMap<>());
            //when, then
            Assertions.assertThatThrownBy(()->boardServiceImpl.getBoard(id)).isInstanceOf(NotFoundException.class);
        }
        @DisplayName("존재하는 게시글을 가져온다.")
        @Test
        public void getFoundBoardTest(){
            //given
            Long id = 100l;
            Map<String, Object> map = new HashMap<>();
            map.put("board",Board.builder().id(100l).member(Member.builder().username("testUsername").build()).title("testTitle").content("testContent").build());
            map.put("commentCount", 0l);
            when(boardRepository.getBoard(id))
                    .thenReturn(map);
            //when
            BoardDTO boardDTO = boardServiceImpl.getBoard(id);
            //then
            Assertions.assertThat(boardDTO.getId()).isEqualTo(100l);
            Assertions.assertThat(boardDTO.getTitle()).isEqualTo("testTitle");
            Assertions.assertThat(boardDTO.getContent()).isEqualTo("testContent");
            Assertions.assertThat(boardDTO.getWriter()).isEqualTo("testUsername");
            Assertions.assertThat(boardDTO.getCommentCount()).isEqualTo(0);
        }
    }
    @Nested
    @DisplayName("게시글을 추천한다.")
    public class recommendBoardTest{
        @Test
        @DisplayName("존재하지 않는 게시글을 추천한다.")
        public void notFountBoardRecommendTest(){
            //given
            Long id = 100l;
            MemberDTO memberDTO = MemberDTO.builder().build();
            when(boardRepository.findById(100l)).thenReturn(Optional.empty());
            //when, then
            Assertions.assertThatThrownBy(()->boardServiceImpl.recommendBoard(id, memberDTO)).isInstanceOf(NotFoundException.class);
        }
        @Test
        @DisplayName("자신이 쓴 게시글을 추천한다.")
        public void selfRecommendBoardTest(){
            //given
            Long id = 1l;
            MemberDTO memberDTO = MemberDTO.builder().username("testUsername").build();
            when(boardRepository.findById(1l)).thenReturn(Optional.of(Board.builder().member(Member.builder().username("testUsername").build()).build()));
            //when,then
            Assertions.assertThatThrownBy(()->boardServiceImpl.recommendBoard(id, memberDTO)).isInstanceOf(AccessDeniedException.class);
        }
        @Test
        @DisplayName("이미 추천한 게시글에 추천한다.")
        public void duplicateRecommendBoardTest(){
            //given
            Long id = 1l;
            MemberDTO memberDTO = MemberDTO.builder().username("testUsername").build();
            Optional<Board> optional = Optional.of(Board.builder().id(1l).member(Member.builder().username("diffUsername").build()).build());
            when(boardRepository.findById(1l)).thenReturn(optional);
            doReturn(true).when(memberBoardRepository).existsByMemberAndBoard(any(Member.class),any(Board.class));
            //when, then
            Assertions.assertThatThrownBy(()->boardServiceImpl.recommendBoard(id, memberDTO)).isInstanceOf(DuplicateException.class);
        }
        @Test
        @DisplayName("일반적인 게시글을 추천한다.")
        public void normalRecommendBoardTest(){
            //given
            Long id = 5l;
            MemberDTO memberDTO = MemberDTO.builder().username("testUsername").build();
            Optional<Board> optional = Optional.of(Board.builder().id(5l).recomendNum(5).member(Member.builder().username("diffUsername").build()).build());
            when(boardRepository.findById(5l)).thenReturn(optional);
            doReturn(false).when(memberBoardRepository).existsByMemberAndBoard(any(Member.class),any(Board.class));
            //when
            Integer recommendNum = boardServiceImpl.recommendBoard(id, memberDTO);
            //then
            Assertions.assertThat(recommendNum).isEqualTo(6);
        }
    }
    @Nested
    @DisplayName("게시글을 삭제한다.")
    public class removeBoardTest{
        @DisplayName("존재하지 않는 게시글을 삭제한다.")
        @Test
        public void removeNotFoundBoardTest(){
            //given
            Long id = 100l;
            MemberDTO memberDTO = MemberDTO.builder().username("testUsername").build();
            when(boardRepository.getBoardWithImage(100l)).thenReturn(Optional.empty());
            //when, then
            Assertions.assertThatThrownBy(()->boardServiceImpl.removeBoard(id, memberDTO)).isInstanceOf(NotFoundException.class);
        }
        @DisplayName("자신이 쓰지않은 게시글을 삭제한다.")
        @Test
        public void removeNotWriteBoardTest(){
            //given
            Long id = 100l;
            MemberDTO memberDTO = MemberDTO.builder().username("testUsername").build();
            Optional<Board> optional = Optional.of(Board.builder().id(100l).member(Member.builder().username("diffUsername").build()).build());
            when(boardRepository.getBoardWithImage(100l)).thenReturn(optional);
            //when, then
            Assertions.assertThatThrownBy(()->boardServiceImpl.removeBoard(id, memberDTO)).isInstanceOf(AccessDeniedException.class);
        }
        @DisplayName("일반적은 게시글을 삭제한다.")
        @Test
        public void normalRemoveBoardTest() throws IOException {
            //given
            Long id = 100l;
            MemberDTO memberDTO = MemberDTO.builder().username("testUsername").build();
            Optional<Board> optional = Optional.of(Board.builder().id(100l).member(Member.builder().username("testUsername").build()).build());
            when(boardRepository.getBoardWithImage(100l)).thenReturn(optional);
            //when, then
            boardServiceImpl.removeBoard(id, memberDTO);
        }
    }

    @Nested
    @DisplayName("게시글을 수정한다.")
    public class modifyBoardTest{
        @DisplayName("존재하지 않은 게시글을 수정한다.")
        @Test
        public void modifyNotFoundBoardTest(){
            //given
            BoardDTO boardDTO = BoardDTO.builder().id(100l).writer("testUsername").title("testTitle").content("testContent").build();
            MemberDTO memberDTO = MemberDTO.builder().username("testUsername").build();
            when(boardRepository.findById(100l)).thenReturn(Optional.empty());
            //when, then
            Assertions.assertThatThrownBy(()->boardServiceImpl.modifyBoard(boardDTO, memberDTO)).isInstanceOf(NotFoundException.class);
        }
        @DisplayName("자신이 쓰지않은 않은 게시글을 수정한다.")
        @Test
        public void modifyNotWriteBoardTest(){
            //given
            BoardDTO boardDTO = BoardDTO.builder().id(100l).writer("diffUsername").title("testTitle").content("testContent").build();
            MemberDTO memberDTO = MemberDTO.builder().username("testUsername").build();
            Board board = boardServiceImpl.dtoToEntity(boardDTO);
            when(boardRepository.findById(100l)).thenReturn(Optional.of(board));
            //when, then
            Assertions.assertThatThrownBy(()->boardServiceImpl.modifyBoard(boardDTO, memberDTO)).isInstanceOf(AccessDeniedException.class);
        }
        @DisplayName("일반적인 게시글을 수정한다.")
        @Test
        public void modifyNormalBoardTest(){
            //given
            BoardDTO boardDTO = BoardDTO.builder().id(100l).writer("testUsername").title("testTitle").content("testContent").build();
            MemberDTO memberDTO = MemberDTO.builder().username("testUsername").build();
            Board board = boardServiceImpl.dtoToEntity(boardDTO);
            when(boardRepository.findById(100l)).thenReturn(Optional.of(board));
            //when, then
            boardServiceImpl.modifyBoard(boardDTO, memberDTO);
        }
    }


}
