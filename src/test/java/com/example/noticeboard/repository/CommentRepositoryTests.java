package com.example.noticeboard.repository;

import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Comment;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.repository.commentRepository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;
    @Test
    public void insertComment(){
        IntStream.rangeClosed(1, 10).forEach(i->{
            Member member = Member.builder()
                    .username("Member"+Long.valueOf((int)(Math.random()*99+1)))
                    .build();
            Board board = Board.builder().id(1L).build();
            Comment parent = Comment.builder()
                    .content("parent..."+i)
                    .board(board)
                    .member(member)
                    .build();
            parent.setParent(parent);

            IntStream.rangeClosed(1, 10).forEach(j->{
                Member member2 = Member.builder()
                        .username("Member"+Long.valueOf((int)(Math.random()*99+1)))
                        .build();
                Comment child = Comment.builder()
                        .content("child..."+j)
                        .board(board)
                        .member(member2)
                        .build();
                parent.addChild(child);
            });
            commentRepository.save(parent);
        });
    }
    @Test
    public void getByBoard(){
        Board board = Board.builder().id(1L).build();
        List<Comment> comments = commentRepository.getCommentList(board, Sort.by("id").descending());
        for (Comment parent : comments) {
            System.out.println("parent: " + parent + " | child_num: " + parent.getChildList().size());
            for(Comment child : parent.getChildList()){
                System.out.println("child = " + child);
            }
        }
    }
    @Test
    public void registerTest(){
        Comment parent = Comment.builder().id(111L).build();
        Board board = Board.builder().id(1L).build();
        Member member = Member.builder().username("Member30").build();
        Comment comment = Comment.builder()
                .parent(parent)
                .board(board)
                .content("registerTest!!!")
                .member(member)
                .build();
        commentRepository.save(comment);
    }
}
