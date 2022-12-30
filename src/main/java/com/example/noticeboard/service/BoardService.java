package com.example.noticeboard.service;


import com.example.noticeboard.dto.BoardDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.dto.PageResultDTO;
import com.example.noticeboard.entity.Board;

public interface BoardService {

    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

    default public BoardDTO entityToDto(Board board, Long commentCount){

        return BoardDTO.builder()
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
    }
}
