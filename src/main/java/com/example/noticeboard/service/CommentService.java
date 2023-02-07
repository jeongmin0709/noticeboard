package com.example.noticeboard.service;

import com.example.noticeboard.dto.CommentDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.member.Member;

import java.util.List;

public interface CommentService {

    List<CommentDTO> getCommentList(Long boardId, String order);

    int recommend(Long commentId);

    Long modify(CommentDTO commentDTO);

    Long remove(Long commentId);

    Long register(CommentDTO commentDTO);

    default CommentDTO entityToDto(Comment comment){

        CommentDTO commentDTO = CommentDTO.builder()
                .id(comment.getId())
                .writer(comment.getMember().getUsername())
                .parentId(comment.getId())
                .content(comment.getContent())
                .recommendNum(comment.getRecomendNum())
                .createDate(comment.getCreateDate())
                .modDate(comment.getModDate())
                .build();

        List<CommentDTO> childList = commentDTO.getChildList();
        comment.getChildList().stream().forEach(child -> {
            CommentDTO childDTO = CommentDTO.builder()
                    .id(child.getId())
                    .writer(child.getMember().getUsername())
                    .parentId(comment.getId())
                    .content(child.getContent())
                    .recommendNum(child.getRecomendNum())
                    .createDate(child.getCreateDate())
                    .modDate(child.getModDate())
                    .build();
            childList.add(childDTO);
        });
        return commentDTO;
    }

    default Comment dtoToEntity(CommentDTO commentDTO){

        Member member = Member.builder().username(commentDTO.getWriter()).build();
        Board board = Board.builder().id(commentDTO.getBoardId()).build();
        Comment parent = null;
        if(!(commentDTO.getParentId() == null)) parent = Comment.builder().id(commentDTO.getParentId()).build();
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
