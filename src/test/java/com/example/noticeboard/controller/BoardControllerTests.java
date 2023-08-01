package com.example.noticeboard.controller;

import com.example.noticeboard.DummyDataProvider;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.dto.PageResultDTO;
import com.example.noticeboard.eannotation.WithMockCustomUser;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(DummyDataProvider.class)
public class BoardControllerTests {

    @Autowired
    MockMvc mockMvc;


    @Test
    @DisplayName("게시글 리스트 요청")
    @WithMockCustomUser
    public void getBoardListTest() throws Exception {
        //given
        String url = "/list";

        //when, then
        mockMvc.perform(get(url).param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("result"))
                .andDo(print());
    }
    @WithMockCustomUser
    @DisplayName("게시글 등록 화면을 요청")
    @Test
    public void getRegisterPageTest() throws Exception {
        String url = "/register";
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("pageRequestDTO"));
    }

}
