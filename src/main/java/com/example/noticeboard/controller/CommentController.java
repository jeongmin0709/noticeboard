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
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CommentDTO>> getList(@PathVariable("boardId") Long boardId){
        log.info("댓글 리스트 요청");
        log.info("게시글 번호: "+ boardId);
        List<CommentDTO> commentDTOList = commentService.getCommnetList(boardId);
        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated() and #writer != principal.username")
    @PostMapping(value = "/recomend")
    public ResponseEntity<Integer> recomend(Long id, String writer){

        log.info("댓글 추천 요청");
        log.info("댓글 번호: "+ id);
        int recommendNum = commentService.recommend(id);
        if(recommendNum == -1){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(recommendNum, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated() and #commentDTO.getWriter() == principal.username")
    @PutMapping
    public ResponseEntity<String> commentModify(@RequestBody CommentDTO commentDTO){

        log.info("댓글 수정 요청");
        log.info("댓글 번호:" + commentDTO);
        commentService.modify(commentDTO);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated() and #writer == principal.username")
    @DeleteMapping
    public ResponseEntity<String> commentModify(Long id, String writer){

        log.info("댓글 삭제 요청");
        log.info("댓글 번호:" + id);
        commentService.remove(id);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Long> commentRegister(@RequestBody CommentDTO commentDTO){
        log.info("댓글 등록 요청");
        log.info("commentDTO" + commentDTO);
        return new ResponseEntity<>(commentService.register(commentDTO), HttpStatus.OK);
    }
}
