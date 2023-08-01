package com.example.noticeboard.repository.commentRepository;

import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    @Modifying
    public void deleteByBoard(Board board);

}
