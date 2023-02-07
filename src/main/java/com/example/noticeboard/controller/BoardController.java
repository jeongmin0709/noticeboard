package com.example.noticeboard.controller;

import com.example.noticeboard.dto.BoardDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.dto.PageResultDTO;
import com.example.noticeboard.security.dto.MemberDTO;
import com.example.noticeboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public String layout(){
        return "redirect:/list";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, @AuthenticationPrincipal MemberDTO memberDTO, Model model){
        log.info("게시글 목록 요청");
        PageResultDTO<BoardDTO, Object[]> pageResultDTO = boardService.getList(pageRequestDTO, memberDTO);
        model.addAttribute("result", pageResultDTO);
        return "/list";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/register")
    public String registerFrom(PageRequestDTO pageRequestDTO, Model model) {
        log.info("게시글 등록 화면 요청");
        model.addAttribute("boardDTO", new BoardDTO());
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        return "/register";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/register")
    public String register(BoardDTO boardDTO, PageRequestDTO pageRequestDTO,RedirectAttributes redirectAttributes){
        log.info("게시글 등록 요청");
        Long boardId = boardService.registerBoard(boardDTO);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("order", pageRequestDTO.getOrder());
        redirectAttributes.addAttribute("my", pageRequestDTO.getMy());
        redirectAttributes.addFlashAttribute("msg", boardId+"번 글이 등록되었습니다.");
        return"redirect:/list";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/read/{id}")
    public String read(@PathVariable Long id, PageRequestDTO pageRequestDTO ,Model model){
        log.info("{}번 게시글 요청", id);
        BoardDTO boardDTO = boardService.getBoard(id);
        model.addAttribute("boardDTO", boardDTO);
        return "/read";
    }

    @PreAuthorize("isAuthenticated() and #writer == principal.username")
    @PostMapping("/remove/{id}")
    public String remove(@PathVariable Long id, String writer, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) throws IOException {
        log.info("{}번 게시글 삭제 요청", id);
        Long boardId = boardService.removeBoard(id);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("order", pageRequestDTO.getOrder());
        redirectAttributes.addAttribute("my", pageRequestDTO.getMy());
        redirectAttributes.addFlashAttribute("msg", boardId+"번 글이 삭제되었습니다.");
        return "redirect:/list";
    }

    @PostAuthorize("isAuthenticated() and #model[boardDTO].writer == principal.username")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable Long id, PageRequestDTO pageRequestDTO ,Model model){
        log.info("{}번 게시글 수정 페이지 요청", id);
        BoardDTO boardDTO = boardService.getBoard(id);
        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        return "/modify";
    }

    @PreAuthorize("isAuthenticated() and #boardDTO.getWriter() == principal.username")
    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes){
        log.info("{}번 게시글 수정 요청", boardDTO.getId());
        Long boardId = boardService.modifyBoard(boardDTO);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("order", pageRequestDTO.getOrder());
        redirectAttributes.addAttribute("my", pageRequestDTO.getMy());
        redirectAttributes.addFlashAttribute("msg", boardId+"번 글이 수정되었습니다.");
        return "redirect:/read/"+boardDTO.getId();
    }

    @PreAuthorize("isAuthenticated() and #writer != principal.username")
    @PatchMapping("/recomend/{id}/{writer}")
    @ResponseBody
    public Integer recommend(@PathVariable Long id, @PathVariable String writer){
        log.info("{}번 게시글 추천 요청", id);
        Integer recommendNum = boardService.recommendBoard(id);
        return recommendNum;
    }
}
