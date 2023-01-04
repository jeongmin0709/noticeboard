package com.example.noticeboard.service;

import com.example.noticeboard.dto.CommentDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentDTO> getCommnetList(Long boardId) {

        Board board = Board.builder().id(boardId).build();
        List<Comment> commentList = commentRepository.getCommentsByBoardOrderById(board);
        return commentList.stream().map(comment -> entityToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public int recommend(Long commentId) {

        Optional<Comment> result = commentRepository.findById(commentId);
        if(result.isPresent()){
            Comment comment = result.get();
            comment.addRecomendNum();
            commentRepository.save(comment);
            return comment.getRecomendNum();
        }
        return -1;
    }

    @Override
    public void modify(CommentDTO commentDTO) {

        Comment comment = dtoToEntity(commentDTO);
        System.out.println("comment = " + comment);
        commentRepository.save(comment);

    }

    @Override
    public void remove(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Long register(CommentDTO commentDTO) {
        Comment comment = dtoToEntity(commentDTO);
        commentRepository.save(comment);
        return comment.getId();
    }
}
