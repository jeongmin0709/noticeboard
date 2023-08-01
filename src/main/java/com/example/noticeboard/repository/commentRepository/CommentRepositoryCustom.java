package com.example.noticeboard.repository.commentRepository;

import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentRepositoryCustom {
    Slice<Map<String, Object>> getCommentListByBoard(Board board, Pageable pageable);
    Slice<Comment> getCommentListByParent(Comment comment, Pageable pageable);
}
