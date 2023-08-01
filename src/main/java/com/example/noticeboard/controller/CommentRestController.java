package com.example.noticeboard.controller;

import com.example.noticeboard.dto.CommentDTO;
import com.example.noticeboard.dto.NotificationDTO;
import com.example.noticeboard.dto.SliceRequestDTO;
import com.example.noticeboard.dto.SliceResultDTO;
import com.example.noticeboard.security.dto.MemberDTO;
import com.example.noticeboard.service.CommentService;
import com.example.noticeboard.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentRestController {

    private final CommentService commentService;

    private final NotificationService notificationService;

    @GetMapping(value = "/{boardId}")
    @PreAuthorize("permitAll()")
    public SliceResultDTO getList(@PathVariable Long boardId, @ModelAttribute SliceRequestDTO sliceRequestDTO){
        log.info("{}번 게시글의 댓글 리스트 요청", boardId);
        return commentService.getCommentList(boardId, sliceRequestDTO);
    }

    @GetMapping("/{id}/child")
    @PreAuthorize("permitAll()")
    public SliceResultDTO getChildList(@PathVariable Long id, @ModelAttribute SliceRequestDTO sliceRequestDTO){
        log.info("{}번 댓글의 답글 리스트 요청", id);
        return commentService.getChildCommentList(id, sliceRequestDTO);
    }

    @PatchMapping(value = "/{id}/recommendNum")
    @PreAuthorize("isAuthenticated()")
    public Integer recommend(@PathVariable Long id, @AuthenticationPrincipal MemberDTO memberDTO){
        log.info("{}번 댓글 추천 요청", id);
        return commentService.recommend(id, memberDTO);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO commentRegister(@RequestBody CommentDTO commentDTO){
        log.info("댓글 등록 요청");
        CommentDTO result = commentService.register(commentDTO);
        return result;
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Long modify(@RequestBody CommentDTO commentDTO, @AuthenticationPrincipal MemberDTO memberDTO){
        log.info("{}번 댓글 수정 요청", commentDTO.getId());
        return commentService.modify(commentDTO, memberDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public Long remove(@PathVariable Long id, @AuthenticationPrincipal MemberDTO memberDTO){
        log.info("{}번 댓글 삭제 요청", id);
        return commentService.remove(id, memberDTO);
    }
}
