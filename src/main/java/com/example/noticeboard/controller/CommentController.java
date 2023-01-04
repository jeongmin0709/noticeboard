package com.example.noticeboard.controller;

import com.example.noticeboard.dto.CommentDTO;
import com.example.noticeboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/noticeboard")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping(value = "/commentList/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentDTO>> getCommentList(@PathVariable("boardId") Long boardId){
        log.info("댓글 리스트 요청");
        log.info("게시글 번호: "+ boardId);
        List<CommentDTO> commentDTOList = commentService.getCommnetList(boardId);
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/commentRecomend/{commentId}")
    public ResponseEntity<Integer> commentRecomend(@PathVariable("commentId") Long commentId){

        log.info("댓글 추천 요청");
        log.info("댓글 번호: "+ commentId);
        int recommendNum = commentService.recommend(commentId);
        if(recommendNum != -1){
            return new ResponseEntity<>(recommendNum, HttpStatus.OK);
        }
        return new ResponseEntity<>(-1, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/comment/{commentId}")
    public ResponseEntity<String> commentModify(@RequestBody CommentDTO commentDTO){

        log.info("댓글 수정 요청");
        log.info("댓글 번호:" + commentDTO);
        commentService.modify(commentDTO);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(value = "/comment/{commentId}")
    public ResponseEntity<String> commentModify(@PathVariable("commentId")Long commentId){

        log.info("댓글 삭제 요청");
        log.info("댓글 번호:" + commentId);
        commentService.remove(commentId);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/comment")
    public ResponseEntity<Long> commentRegister(@RequestBody CommentDTO commentDTO){
        log.info("댓글 등록 요청");
        log.info("commentDTO" + commentDTO);
        return new ResponseEntity<>(commentService.register(commentDTO), HttpStatus.OK);
    }
}
