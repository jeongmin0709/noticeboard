package com.example.noticeboard.controller;

import com.example.noticeboard.dto.CommentDTO;
import com.example.noticeboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentRestController {

    private final CommentService commentService;

    @PreAuthorize("permitAll()")
    @GetMapping(value = "/list/{boardId}/{order}")
    public List<CommentDTO> getList(@PathVariable Long boardId, @PathVariable String order){
        log.info("{}번 게시글의 댓글 리스트 요청", boardId);
        return commentService.getCommentList(boardId, order);
    }

    @PreAuthorize("isAuthenticated() and #writer != principal.username")
    @PatchMapping(value = "/{id}/{writer}")
    public Integer recommend(@PathVariable Long id, @PathVariable String writer){
        log.info("{}번 댓글 추천 요청", id);
        return commentService.recommend(id);
    }

    @PreAuthorize("isAuthenticated() and #commentDTO.getWriter() == principal.username")
    @PutMapping
    public Long modify(@RequestBody CommentDTO commentDTO){
        log.info("{}번 댓글 수정 요청", commentDTO.getId());
        return commentService.modify(commentDTO);
    }

    @PreAuthorize("isAuthenticated() and #writer == principal.username")
    @DeleteMapping("/{id}/{writer}")
    public Long remove(@PathVariable Long id, @PathVariable String writer){
        log.info("{}번 댓글 삭제 요청", id);
        return commentService.remove(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long commentRegister(@RequestBody CommentDTO commentDTO){
        log.info("댓글 등록 요청");
        log.info(commentDTO.toString());
        return commentService.register(commentDTO);
    }
}
