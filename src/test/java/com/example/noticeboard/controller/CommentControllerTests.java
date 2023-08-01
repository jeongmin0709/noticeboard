package com.example.noticeboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTests {

    @Autowired
    CommentRestController controller;

    @Autowired
    MockMvc mockMvc;

    protected MockHttpSession session;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws Exception{
        session = new MockHttpSession();

        session.setAttribute("name", "테스트");
    }

    @Test
    public void testGetCommentList() throws Exception{
        //given
        String url = "/comment/list/1/id";
        //when, then
        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void TestRegister() throws Exception{
        //given
        String url = "/comment";
        Map<String, String> data = new HashMap<>();
        data.put("bardId", "1");
        data.put("content", "register Test2!!");
        data.put("writer", "Member30");
        data.put("parentId", "111");
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(data))
        ).andExpect(status().isCreated()).andDo(print());
    }
}
