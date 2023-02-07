package com.example.noticeboard.service;

import com.example.noticeboard.dto.CommentDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.exception.custom_exception.CommentNotFoundException;
import com.example.noticeboard.repository.commentRepository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public List<CommentDTO> getCommentList(Long boardId, String order) {

        Sort sort;
        if(order.equals("new")) sort = Sort.by("createDate").descending();
        else if(order.equals("old")) sort  = Sort.by("createDate").ascending();
        else if(order.equals("recomend")) sort = Sort.by("recomendNum").descending();
        else sort = Sort.by("id").descending();

        Board board = Board.builder().id(boardId).build();
        List<Comment> commentList = commentRepository.getCommentList(board, sort);
        return commentList.stream().map(comment -> entityToDto(comment)).collect(Collectors.toList());
    }

    @Override
    public int recommend(Long id) {
        Optional<Comment> result = commentRepository.findById(id);
        if(result.isEmpty()) throw new CommentNotFoundException("존재하지 않는 댓글입니다.");
        Comment comment = result.get();
        comment.addRecomendNum();
        return comment.getRecomendNum();
    }

    @Override
    public Long modify(CommentDTO commentDTO) {
        Optional<Comment> result = commentRepository.findById(commentDTO.getId());
        if(result.isEmpty()) throw new CommentNotFoundException("존재하지 않는 댓글입니다.");
        Comment comment = result.get();
        comment.changeContent(commentDTO.getContent());
        return comment.getId();
    }

    @Override
    public Long remove(Long id) {
        try {
            commentRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new CommentNotFoundException("존재하지 않는 댓글입니다.");
        }
        return id;
    }

    @Override
    public Long register(CommentDTO commentDTO) {
        Comment comment = dtoToEntity(commentDTO);
        log.info(comment.toString());
        commentRepository.save(comment);
        return comment.getId();
    }
}
