package com.example.noticeboard.repository;

import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{

    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Comment> getCommentsByBoardOrderById(Board board);
    void deleteByBoard(Board board);
}
