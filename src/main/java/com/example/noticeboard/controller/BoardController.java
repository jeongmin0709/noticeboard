package com.example.noticeboard.controller;

import com.example.noticeboard.dto.BoardDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.dto.PageResultDTO;
import com.example.noticeboard.dto.PagingBoardDTO;
import com.example.noticeboard.entity.Board;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    @PreAuthorize("permitAll()")
    public String layout(){
        return "redirect:list";
    }

    @GetMapping("/list")
    @PreAuthorize("permitAll()")
    public String list(PageRequestDTO pageRequestDTO, @AuthenticationPrincipal MemberDTO memberDTO, Model model){
        log.info("게시글 목록 화면 요청");
        PageResultDTO<PagingBoardDTO, Board> pageResultDTO = boardService.getList(pageRequestDTO, memberDTO);
        model.addAttribute("result", pageResultDTO);
        return "list";
    }

    @GetMapping("/register")
    @PreAuthorize("isAuthenticated()")
    public String registerFrom(PageRequestDTO pageRequestDTO, Model model) {
        log.info("게시글 등록 화면 요청");
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        return "register";
    }

    @GetMapping("/read/{id}")
    @PreAuthorize("permitAll()")
    public String read(@PathVariable Long id,
                       PageRequestDTO pageRequestDTO,
                       HttpServletRequest request,
                       HttpServletResponse response,
                       Model model){
        log.info("{}번 게시글 화면 요청", id);
        BoardDTO boardDTO = boardService.getBoard(id);
        increaseViewNum(id, request, response);
        model.addAttribute("boardDTO", boardDTO);
        return "read";
    }

    @GetMapping("/modify/{id}")
    @PostAuthorize("isAuthenticated() and #model[boardDTO].writer == principal.username") //요청한 클라이언트가 게시글 작성자인지 클라이언트에 응답하기 직전에 권한검사
    public String modify(@PathVariable Long id, PageRequestDTO pageRequestDTO ,Model model){
        log.info("{}번 게시글 수정 페이지 요청", id);
        BoardDTO boardDTO = boardService.getBoard(id);
        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("pageRequestDTO", pageRequestDTO);
        return "modify";
    }


    @PostMapping("/boards")
    @PreAuthorize("isAuthenticated()")
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

    @DeleteMapping("/boards/{id}")
    @PreAuthorize("isAuthenticated()")
    public String remove(@PathVariable Long id,
                         @AuthenticationPrincipal MemberDTO memberDTO,
                         PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes) throws IOException {
        log.info("{}번 게시글 삭제 요청", id);
        boardService.removeBoard(id , memberDTO);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("order", pageRequestDTO.getOrder());
        redirectAttributes.addAttribute("my", pageRequestDTO.getMy());
        redirectAttributes.addFlashAttribute("message", id+"번 글이 삭제되었습니다.");
        return "redirect:/list";
    }


    @PutMapping("/boards/{id}")
    @PreAuthorize("isAuthenticated()")
    public String modify(BoardDTO boardDTO,
                         PageRequestDTO pageRequestDTO,
                         @AuthenticationPrincipal MemberDTO memberDTO,
                         RedirectAttributes redirectAttributes){
        log.info("{}번 게시글 수정 요청", boardDTO.getId());
        boardService.modifyBoard(boardDTO, memberDTO);
        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("order", pageRequestDTO.getOrder());
        redirectAttributes.addAttribute("my", pageRequestDTO.getMy());
        redirectAttributes.addFlashAttribute("msg", boardDTO.getId()+"번 글이 수정되었습니다.");
        return "redirect:/read/"+boardDTO.getId();
    }

    // 조회수 중복 방지 함수(쿠키 사용)
    private void increaseViewNum(Long id,HttpServletRequest request, HttpServletResponse response){
        Cookie oldCookie = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) { // 쿠기가 null이아니라면
            for (Cookie cookie : cookies) { //boardView라는 쿠키가 있는지 검사
                if (cookie.getName().equals("boardView")) {
                    oldCookie = cookie;
                }
            }
        }
        if (oldCookie != null) { //boardView 라는 쿠키가 존재한다면
            if (!oldCookie.getValue().contains("[" + id.toString() + "]")) { // boardView 쿠키의 값이 조회한 게시글 아이디라면
                boardService.increaseViewNum(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else { // boardView 라는 쿠키가 존재하지 않는다면
            boardService.increaseViewNum(id);
            Cookie newCookie = new Cookie("boardView","[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
    }

}
