package com.example.noticeboard.controller;

import com.example.noticeboard.dto.BoardDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.dto.PageResultDTO;
import com.example.noticeboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/noticeboard")
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
        return"redirect:/noticeboard/list";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/read")
    public void read(Long id, PageRequestDTO pageRequestDTO ,Model model){
        log.info("게시글 요청: " + id);
        BoardDTO boardDTO = boardService.getBoard(id, "read");
        model.addAttribute("BoardDTO", boardDTO);
    }

    @PostMapping("/remove")
    public String remove(Long id, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes){
        log.info("게시글 삭제 요청: "+ id);
        boardService.removeBoard(id);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        return "redirect:/noticeboard/list";
    }

    @GetMapping("/modify")
    public void modify(Long id, PageRequestDTO pageRequestDTO ,Model model){
        log.info("게시글 수정 페이지 요청: " + id);
        BoardDTO boardDTO = boardService.getBoard(id, "modify");
        model.addAttribute("BoardDTO", boardDTO);
    }

    @PostMapping("/modify")
    public String modify(BoardDTO boardDTO, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes){
        log.info("게시글 수정 요청: " + boardDTO.getId());
        log.info(boardDTO);
        redirectAttributes.addAttribute("id", boardDTO.getId());
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        boardService.modifyBoard(boardDTO);
        return "redirect:/noticeboard/read";
    }

    @GetMapping("/recomend/{id}")
    @ResponseBody
    public int recomend(@PathVariable Long id){
        log.info("게시글 추천 요청");
        return boardService.recomendBoard(id);
    }
}
