package com.example.noticeboard.controller;

import com.example.noticeboard.dto.BoardDTO;
import com.example.noticeboard.dto.NotificationDTO;
import com.example.noticeboard.security.dto.MemberDTO;
import com.example.noticeboard.service.BoardService;
import com.example.noticeboard.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BoardRestController {

    private final BoardService boardService;

    @PatchMapping("/boards/{id}/recommendNum")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public Integer recommend(@PathVariable Long id, @AuthenticationPrincipal MemberDTO memberDTO) throws AuthenticationException{
        log.info("{}번 게시글 추천 요청", id);
        return boardService.recommendBoard(id, memberDTO);
    }
}
