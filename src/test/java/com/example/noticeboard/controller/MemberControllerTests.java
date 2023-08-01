package com.example.noticeboard.controller;

import com.example.noticeboard.DummyDataProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(DummyDataProvider.class)
public class MemberControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("아이디 찾기 테스트")
    @WithAnonymousUser
    @Test
    public void findUsernameTest() throws Exception {
        //given
        String url = "/member/username/find";
        Map<String, String> map = new HashMap<>();
        map.put("email", "user1@gmail.com");
        map.put("code" , "123412");
        String data = objectMapper.writeValueAsString(map);
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(data))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
