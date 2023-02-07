package com.example.noticeboard.repository;

import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.member.Member;
import com.example.noticeboard.repository.boardrepository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    BoardRepository boardRepository;

    @Test
    public void insertBoard(){
        IntStream.rangeClosed(1, 200).forEach(i->{
            Member member = Member.builder()
                    .username("Member"+Long.valueOf((int)(Math.random()*99+1))).build();
            Board board = Board.builder()
                    .title("title..."+ i)
                    .member(member)
                    .content("content..."+ i)
                    .build();
            boardRepository.save(board);
        });
    }
//    @Test
//    public void findOne(){
//        Long id = 204L;
//        Board board = boardRepository.getBoardWithMember(id);
//        System.out.println("result = " + board.getImageList());
////        System.out.println("image = " + board.getImageList());
////        System.out.println("count = " + count);
//    }
    @Test
    public void getBoardList(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Object[]> result = boardRepository.getBoardPage(null, null,null , null,pageable);
        result.forEach(row->{
            Object[] arr = (Object[]) row;
            System.out.println(Arrays.toString(arr));
        });
    }

}
