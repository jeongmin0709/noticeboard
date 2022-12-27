package com.example.noticeboard.controller;

import com.example.noticeboard.dto.BoardDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.dto.PageResultDTO;
import com.example.noticeboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/noticeboard")
@RequiredArgsConstructor
@Log4j2
public class BoardController {

    private final BoardService boardService;
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        PageResultDTO<BoardDTO, Object[]> pageResultDTO = boardService.getList(pageRequestDTO);
        log.info(pageResultDTO);
        model.addAttribute("result", boardService.getList(pageRequestDTO));
    }
}
