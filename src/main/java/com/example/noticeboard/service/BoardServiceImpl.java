package com.example.noticeboard.service;

import com.example.noticeboard.dto.*;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Image;
import com.example.noticeboard.exception.custom_exception.BoardNotFoundException;
import com.example.noticeboard.repository.boardrepository.BoardRepository;
import com.example.noticeboard.repository.ImageRepository;
import com.example.noticeboard.repository.commentRepository.CommentRepository;
import com.example.noticeboard.security.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final ImageRepository imageRepository;

    private final ImageService imageService;


    // 게시글 리스트 가져오기
    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO, MemberDTO memberDTO) {
        Function<Object[], BoardDTO> fn = (en -> entityToDto((Board)en[0] ,(Long)en[1]));
        Page<Object[]> result = boardRepository.getBoardPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getMy(),
                memberDTO == null?null:memberDTO.getUsername(),
                pageRequestDTO.getPageable());
        return new PageResultDTO<>(result, fn);
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
    public BoardDTO getBoard(Long id) {
        Optional<Board> result = boardRepository.getBoardWithAll(id);
        if(result.isEmpty()) throw new BoardNotFoundException("존재하지 않는 게시글 입니다.");
        Board board = result.get();
        board.addViewNum();
        return entityToDto(board, 0L);
    }

    //게시글 추천
    @Override
    public Integer recommendBoard(Long id) {
        Optional<Board> result = boardRepository.findById(id);
        if(result.isEmpty()) throw new BoardNotFoundException("존재하지 않는 게시글 입니다.");
        Board board = result.get();
        board.addRecommendNum();
        return board.getRecomendNum();
    }
    //게시글 삭제
    @Override
    public Long removeBoard(Long id) throws IOException {
        Optional<Board> result = boardRepository.getBoardWithImage(id);
        if(result.isEmpty()) throw new BoardNotFoundException("존재하지 않는 게시글 입니다.");
        Board board = result.get();
        for(Image image:board.getImageList())  imageService.removeImg(image.getImageURL());
        boardRepository.deleteById(id);
        return board.getId();
    }

    //게시글 수정
    @Override
    public Long modifyBoard(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getId());
        if(result.isEmpty()) throw new BoardNotFoundException("존재하지 않는 게시글 입니다.");
        Board board = result.get();
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.removeImage(); // 연관관계 삭제
        imageRepository.deleteByBoard(board); // db 에서 데이터 삭제
        boardDTO.getImageDTOList().stream().forEach(imageDTO -> {
            board.addImage(
                    Image.builder()
                            .name(imageDTO.getFileName())
                            .path(imageDTO.getFolderPath())
                            .uuid(imageDTO.getUuid())
                            .build()
            );
        });
        return board.getId();
    }
}
