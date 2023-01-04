package com.example.noticeboard.service;

import com.example.noticeboard.dto.BoardDTO;
import com.example.noticeboard.dto.ImageDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.dto.PageResultDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Image;
import com.example.noticeboard.repository.CommentRepository;
import com.example.noticeboard.repository.boardrepository.BoardRepository;
import com.example.noticeboard.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    private final CommentRepository commentRepository;

    private final ImageRepository imageRepository;

    // 게시글 리스트 가져오기
    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        Function<Object[], BoardDTO> fn = (en -> entityToDto((Board)en[0] ,(Long)en[1]));
        Page<Object[]> result = boardRepository.getBoardPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("id").descending()));
        return new PageResultDTO<>(result, fn);
    }

    // 게시글 등록
    @Override
    public Long registerBoard(BoardDTO boardDTO) {

        Map<String, Object> entityMap = dtoToEntity(boardDTO);
        Board board = (Board) entityMap.get("board");
        List<Image> imageList = (List<Image>) entityMap.get("imageList");
        boardRepository.save(board);
        imageList.forEach(image -> imageRepository.save(image));
        return board.getId();

    }

    //게시글 가져오기
    @Override
    public BoardDTO getBoard(Long id, String pageType) {

        List<Object[]> result = boardRepository.getBoardWithAll(id);
        Board board = (Board) result.get(0)[0];
        List<Image> imageList = new ArrayList<>();
        result.forEach(ary->{
            if(ary[1] != null) {
                Image image = (Image) ary[1];
                imageList.add(image);
            }
        });
        if(pageType.equals("read")){
            board.addViewNum();
        }
        return entityToDto(board, imageList);
    }

    @Override
    public int recomendBoard(Long id) {
        Optional<Board> result = boardRepository.findById(id);
        Board board = result.get();
        board.addRecommendNum();
        return board.getRecomendNum();
    }
    //게시글 삭제
    @Override
    public void removeBoard(Long id) {
        Board board = Board.builder().id(id).build();
        imageRepository.deleteByBoard(board);
        commentRepository.deleteByBoard(board);
        boardRepository.deleteById(id);
    }

    //게시글 수정
    @Override
    public void modifyBoard(BoardDTO boardDTO) {

        Optional<Board> result = boardRepository.findById(boardDTO.getId());
        if(result.isPresent()){
            Board board = result.get();
            imageRepository.deleteByBoard(board);
            imageRepository.flush();
            boardDTO.getImageDTOList().forEach(imageDTO -> {
                Image image = Image.builder()
                        .board(board)
                        .name(imageDTO.getFileName())
                        .path(imageDTO.getFolderPath())
                        .uuid(imageDTO.getUuid())
                        .build();
                imageRepository.save(image);
            });
            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent());
            boardRepository.save(board);
        }
    }
}
