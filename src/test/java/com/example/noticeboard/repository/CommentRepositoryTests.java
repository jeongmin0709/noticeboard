package com.example.noticeboard.repository;

import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;
    @Test
    public void insertComment(){
        IntStream.rangeClosed(1, 300).forEach(i->{
            Member member = Member.builder()
                    .username("Member"+Long.valueOf((int)(Math.random()*99+1)))
                    .build();
            Board board = Board.builder().id(Long.valueOf((int)(Math.random()*199+1))).build();
            Comment comment = Comment.builder()
                    .content("content..."+i)
                    .board(board)
                    .member(member)
                    .build();
            commentRepository.save(comment);
        });
    }
    @Test
    public void getByBoard(){
        Board board = Board.builder().id(100L).build();
        List<Comment> comments = commentRepository.getCommentsByBoardOrderById(board);
        for (Comment comment : comments) {
            System.out.println("comment.getMember(). = " + comment.getMember().getUsername());
        }
    }
}
