package com.example.noticeboard.service;

import com.example.noticeboard.dto.CommentDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.member.Member;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getCommnetList(Long boardId);

    int recommend(Long commentId);

    void modify(CommentDTO commentDTO);

    void remove(Long commentId);

    Long register(CommentDTO commentDTO);

    default public CommentDTO entityToDto(Comment comment){

        return CommentDTO.builder()
                .id(comment.getId())
                .writer(comment.getMember().getUsername())
                .content(comment.getContent())
                .recomendNum(comment.getRecomendNum())
                .createDate(comment.getCrateDate())
                .modDate(comment.getModDate())
                .build();
    }

    default public Comment dtoToEntity(CommentDTO commentDTO){

        Member member = Member.builder().username(commentDTO.getWriter()).build();
        Board board = Board.builder().id(commentDTO.getBoardId()).build();
        return Comment.builder()
                .id(commentDTO.getId())
                .board(board)
                .member(member)
                .content(commentDTO.getContent())
                .recomendNum(commentDTO.getRecomendNum())
                .build();
    }

}
