package com.example.noticeboard.service;


import com.example.noticeboard.dto.*;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Image;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.security.dto.MemberDTO;

import java.io.IOException;

public interface BoardService {

    PageResultDTO<PagingBoardDTO, Board> getList(PageRequestDTO pageRequestDTO, MemberDTO memberDTO);

    Long registerBoard(BoardDTO boardDTO);

    BoardDTO getBoard(Long id);

    Integer recommendBoard(Long id, MemberDTO recommender);

    void removeBoard(Long id, MemberDTO memberDTO) throws IOException;

    void modifyBoard(BoardDTO boardDTO ,MemberDTO memberDTO);

    void increaseViewNum(Long id);

    default public BoardDTO entityToDto(Board board){

        BoardDTO boardDTO = BoardDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getMember().getUsername())
                .recomendNum(board.getRecomendNum())
                .viewNum(board.getViewNum())
                .createDate(board.getCreateDate())
                .modDate(board.getModDate())
                .build();

        board.getImageList().forEach(image -> {
                    ImageDTO imageDTO = ImageDTO.builder()
                            .boardId(board.getId())
                            .uuid(image.getUuid())
                            .folderPath(image.getPath())
                            .fileName(image.getName())
                            .build();
                    boardDTO.getImageDTOList().add(imageDTO);
                }
        );
        return boardDTO;
    }


    default Board dtoToEntity(BoardDTO boardDTO){

        Member member = Member.builder().username(boardDTO.getWriter()).build();
        Board board = Board.builder()
                .id(boardDTO.getId())
                .member(member)
                .recomendNum(boardDTO.getRecomendNum())
                .viewNum(boardDTO.getViewNum())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .build();

        boardDTO.getImageDTOList().forEach(imageDTO -> {
            board.addImage(
              Image.builder()
                      .name(imageDTO.getFileName())
                      .path(imageDTO.getFolderPath())
                      .uuid(imageDTO.getUuid())
                      .build());
        });
        return board;
    }
}
