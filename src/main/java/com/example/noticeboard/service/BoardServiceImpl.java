package com.example.noticeboard.service;

import com.example.noticeboard.dto.*;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Image;
import com.example.noticeboard.entity.MemberBoard;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.entity.notification.NotificationType;
import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.exception.custom_exception.BoardNotfoundException;
import com.example.noticeboard.exception.custom_exception.DuplicateException;
import com.example.noticeboard.exception.custom_exception.AccessDeniedException;
import com.example.noticeboard.exception.custom_exception.SelfRecommendException;
import com.example.noticeboard.repository.MemberBoardRepository;
import com.example.noticeboard.repository.boardrepository.BoardRepository;
import com.example.noticeboard.security.dto.MemberDTO;
import com.example.noticeboard.service.event.ModifyBoardEvent;
import com.example.noticeboard.service.event.NotificationEvent;
import com.example.noticeboard.service.event.RemoveBoardEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final MemberBoardRepository memberBoardRepository;

    private final ApplicationEventPublisher publisher;



    // 게시글 리스트 가져오기
    @Override
    @Transactional(readOnly = true)
    public PageResultDTO<PagingBoardDTO, Board> getList(PageRequestDTO pageRequestDTO, MemberDTO memberDTO) {
        Page<PagingBoardDTO> result = boardRepository.getBoardPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getMy(),
                memberDTO == null?null:memberDTO.getUsername(),
                pageRequestDTO.getPageable());
        return new PageResultDTO<>(result);
    }


    // 게시글 등록
    @Override
    public Long registerBoard(BoardDTO boardDTO) {
        Board board = dtoToEntity(boardDTO);
        boardRepository.save(board);
        return board.getId();
    }

    //게시글 가져오기
    @Override
    @Transactional(readOnly = true)
    public BoardDTO getBoard(Long id) {
        //현재글을 댓글수와함께 조회
        Map<String, Object> result = boardRepository.getBoard(id);
        if(result.get("board") == null) throw new BoardNotfoundException(ErrorCode.NOT_FOUND_BOARD);
        Board board = (Board)result.get("board");
        Long commentCount = (Long) result.get("commentCount");
        BoardDTO boardDTO = entityToDto(board);
        boardDTO.setCommentCount(commentCount.intValue());

        //이전글 다음글을 조회
        Map<String, Board> prevAndNextBoard = boardRepository.getPrevAndNextBoard(id);
        Board prevBoard = prevAndNextBoard.get("prev");
        Board nextBoard = prevAndNextBoard.get("next");
        if(prevBoard != null){
            boardDTO.setPrevId(prevBoard.getId());
            boardDTO.setPrevTitle(prevBoard.getTitle());
        }
        if(nextBoard != null){
            boardDTO.setNextId(nextBoard.getId());
            boardDTO.setNextTitle(nextBoard.getTitle());
        }
        return boardDTO;
    }

    public void increaseViewNum(Long id){
        Optional<Board> result = boardRepository.findById(id);
        if(result.isEmpty()) throw new BoardNotfoundException(ErrorCode.NOT_FOUND_BOARD);
        Board board = result.get();
        board.addViewNum();
    }

    //게시글 추천
    @Override
    public Integer recommendBoard(Long id, MemberDTO recommender) {

        Member member = Member.builder().username(recommender.getUsername()).build(); // 추천인

        Optional<Board> result = boardRepository.findById(id);

        // 게시글이 존재하지 않으면
        if(result.isEmpty()) throw new BoardNotfoundException(ErrorCode.NOT_FOUND_BOARD);
        Board board = result.get(); // 게시글

        // 자신이 쓴 게시글이라면 추천 x
        if(board.getMember().getUsername().equals(recommender.getUsername())) throw new SelfRecommendException(ErrorCode.SELF_RECOMMEND);

        // 이미 게시글에 추천을 했다면 추천 x
        if(memberBoardRepository.existsByMemberAndBoard(member, board)) throw new DuplicateException(ErrorCode.DUPLICATE_RECOMMEND);

        board.addRecommendNum(); // 게시글의 추천수 증가

        MemberBoard memberBoard = MemberBoard.builder().member(member).board(board).build();
        memberBoardRepository.save(memberBoard); // 추천인이 게시글 추천한 것을 저장

        //알림 이벤트 발행
        publisher.publishEvent(new NotificationEvent(board, recommender.getUsername(), NotificationType.BOARD_RECOMMEND));

        return board.getRecomendNum();

    }

    //게시글 삭제
    @Override
    public void removeBoard(Long id, MemberDTO memberDTO) throws IOException {
        Optional<Board> result = boardRepository.getBoardWithImage(id);
        if(result.isEmpty()) throw new BoardNotfoundException(ErrorCode.NOT_FOUND_BOARD); // 게시글이 존재하지 않으면
        Board board = result.get();
        if(!memberDTO.getUsername().equals(board.getMember().getUsername())) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        publisher.publishEvent(new RemoveBoardEvent(board));
        memberBoardRepository.deleteByBoard(board);
        boardRepository.deleteById(id);
    }

    //게시글 수정
    @Override
    public void modifyBoard(BoardDTO boardDTO, MemberDTO memberDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getId());
        if(result.isEmpty()) throw new BoardNotfoundException(ErrorCode.NOT_FOUND_BOARD);
        Board board = result.get();
        if(!memberDTO.getUsername().equals(board.getMember().getUsername())) throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        publisher.publishEvent(new ModifyBoardEvent(board));
        board.removeImage();
        boardDTO.getImageDTOList().stream().forEach(imageDTO -> {
            board.addImage(
                    Image.builder()
                            .name(imageDTO.getFileName())
                            .path(imageDTO.getFolderPath())
                            .uuid(imageDTO.getUuid())
                            .build()
            );
        });
    }
}
