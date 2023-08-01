package com.example.noticeboard.service;

import com.example.noticeboard.dto.CommentDTO;
import com.example.noticeboard.dto.SliceRequestDTO;
import com.example.noticeboard.dto.SliceResultDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.security.dto.MemberDTO;
import com.example.noticeboard.service.event.RemoveBoardEvent;

import java.util.List;

public interface CommentService {

    SliceResultDTO getCommentList(Long boardId, SliceRequestDTO sliceRequestDTO);

    SliceResultDTO getChildCommentList(Long commentId, SliceRequestDTO sliceRequestDTO);

    Integer recommend(Long commentId, MemberDTO recommender);

    Long modify(CommentDTO commentDTO, MemberDTO memberDTO);

    Long remove(Long commentId, MemberDTO memberDTO);

    CommentDTO register(CommentDTO commentDTO);


    default CommentDTO entityToDto(Comment comment, Long childCount){

        CommentDTO commentDTO = CommentDTO.builder()
                .id(comment.getId())
                .writer(comment.getMember().getUsername())
                .boardId(comment.getBoard().getId())
                .parentId(comment.getParent() == null?null: comment.getParent().getId())
                .childCount(childCount.intValue())
                .content(comment.getContent())
                .recommendNum(comment.getRecomendNum())
                .createDate(comment.getCreateDate())
                .modDate(comment.getModDate())
                .build();
        return commentDTO;
    }


    default Comment dtoToEntity(CommentDTO commentDTO){

        Member member = Member.builder().username(commentDTO.getWriter()).build();
        Board board = Board.builder().id(commentDTO.getBoardId()).build();
        Comment parent = null;
        if(commentDTO.getParentId() != null) parent = Comment.builder().id(commentDTO.getParentId()).build();
        return Comment.builder()
                .id(commentDTO.getId())
                .board(board)
                .parent(parent)
                .member(member)
                .content(commentDTO.getContent())
                .recomendNum(commentDTO.getRecommendNum())
                .build();
    }

}
