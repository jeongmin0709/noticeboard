package com.example.noticeboard.service;


import com.example.noticeboard.dto.BoardDTO;
import com.example.noticeboard.dto.ImageDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.dto.PageResultDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Image;
import com.example.noticeboard.entity.member.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface BoardService {

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    Long registerBoard(BoardDTO boardDTO);

    BoardDTO getBoard(Long id, String pageType);

    int recomendBoard(Long id);

    void removeBoard(Long id);

    void modifyBoard(BoardDTO boardDTO);

    default public BoardDTO entityToDto(Board board, List<Image> imageList){

        BoardDTO boardDTO = BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getMember().getUsername())
                .recomendNum(board.getRecomendNum())
                .viewNum(board.getViewNum())
                .createDate(board.getCrateDate())
                .modDate(board.getModDate())
                .build();

        List<ImageDTO> imageDTOList = imageList.stream().map(image -> {
            return ImageDTO.builder()
                    .boardId(board.getId())
                    .fileName(image.getName())
                    .folderPath(image.getPath())
                    .uuid(image.getUuid())
                    .build();
        }).collect(Collectors.toList());

        boardDTO.setImageDTOList(imageDTOList);
        return boardDTO;
    }

    default public BoardDTO entityToDto(Board board, Long commentCount){

        BoardDTO boardDTO = BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getMember().getUsername())
                .recomendNum(board.getRecomendNum())
                .viewNum(board.getViewNum())
                .commentCount(commentCount.intValue())
                .createDate(board.getCrateDate())
                .modDate(board.getModDate())
                .build();

        return boardDTO;
    }

    default public Map<String, Object> dtoToEntity(BoardDTO boardDTO){

        Map<String, Object> entityMap = new HashMap<>();

        Member member = Member.builder().username(boardDTO.getWriter()).build();
        Board board = Board.builder()
                .id(boardDTO.getId())
                .member(member)
                .recomendNum(boardDTO.getRecomendNum())
                .viewNum(boardDTO.getViewNum())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .build();
        entityMap.put("board", board);

        List<Image> imageList = boardDTO.getImageDTOList().stream().map(imageDTO -> {
            return Image.builder()
                    .board(board)
                    .path(imageDTO.getFolderPath())
                    .name(imageDTO.getFileName())
                    .uuid(imageDTO.getUuid())
                    .build();
        }).collect(Collectors.toList());
        entityMap.put("imageList", imageList);

        return entityMap;
    }
}
