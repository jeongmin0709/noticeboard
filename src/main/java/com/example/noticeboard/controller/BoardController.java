package com.example.noticeboard.controller;

import com.example.noticeboard.dto.BoardDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.dto.PageResultDTO;
import com.example.noticeboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BoardController {

    private final BoardService boardService;

    @PreAuthorize("permitAll()")
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("게시글 리스트 요청");
        PageResultDTO<BoardDTO, Object[]> pageResultDTO = boardService.getList(pageRequestDTO);
        model.addAttribute("result", pageResultDTO);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/register")
    public void registerFrom(PageRequestDTO pageRequestDTO){
        log.info("게시글 등록 화면 요청");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/register")
    public String register(BoardDTO boardDTO){
        log.info("게시글 등록 요청");
        Long boardId = boardService.registerBoard(boardDTO);
        return"redirect:/list";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/read")
    public void read(Long id, PageRequestDTO pageRequestDTO ,Model model){
        log.info("게시글 요청: " + id);
        BoardDTO boardDTO = boardService.getBoard(id, "read");
        model.addAttribute("BoardDTO", boardDTO);
    }

    @PreAuthorize("isAuthenticated() and #writer == principal.username")
    @PostMapping("/remove")
    public String remove(Long id, String writer, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes){
        log.info("게시글 삭제 요청: "+ id);
        boardService.removeBoard(id);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        return "redirect:/list";
    }

    @PostAuthorize("isAuthenticated() and #model[BoardDTO].writer == principal.username")
    @GetMapping("/modify")
    public void modify(Long id, PageRequestDTO pageRequestDTO ,Model model){
        log.info("게시글 수정 페이지 요청: " + id);
        BoardDTO boardDTO = boardService.getBoard(id, "modify");
        model.addAttribute("BoardDTO", boardDTO);
    }

    @PreAuthorize("isAuthenticated() and #boardDTO.getWriter() == principal.username")
    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes){
        log.info("게시글 수정 요청: " + boardDTO.getId());
        log.info(boardDTO);
        redirectAttributes.addAttribute("id", boardDTO.getId());
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        boardService.modifyBoard(boardDTO);
        return "redirect:/read";
    }

    @PreAuthorize("isAuthenticated() and #writer != principal.username")
    @PostMapping("/recomend")
    @ResponseBody
    public ResponseEntity<Integer> recomend(Long id, String writer){
        log.info("게시글 추천 요청");
        return new ResponseEntity<>(boardService.recomendBoard(id), HttpStatus.OK);
    }
}
