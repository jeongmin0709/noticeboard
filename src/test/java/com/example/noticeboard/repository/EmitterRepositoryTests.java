package com.example.noticeboard.repository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public class EmitterRepositoryTests {

    private EmitterRepository emitterRepository = new EmitterRepositoryImpl();
    private Long DEFAULT_TIMEOUT = 60L * 1000L * 60L;

    @Test
    @DisplayName("새로운 Emitter를 추가한다.")
    void saveEmitterTest(){
        //given
        String emitterId = "tjrwjdals999";
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        //when, then
        Assertions.assertDoesNotThrow(()-> emitterRepository.save(emitterId,sseEmitter));
    }
    @Test
    @DisplayName("유저이름으로 Emitter를 찾는다.")
    void findEmitterTest(){
        //given
        String username = "tjrwjdals999";
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(username, sseEmitter);
        //when
        Map<String, SseEmitter> allStartWithByUsername = emitterRepository.findAllStartWithByUsername(username);
        //then
        //Assertions.assertEquals(sseEmitter, emitterById);
    }
    @Test
    @DisplayName("유저이름으로 Emitter를 삭제한다.")
    void removeEmitterTest(){
        //given
        String username = "tjrwjdals999";
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(username, sseEmitter);
        //when
        emitterRepository.deleteById(username);
        //then
//        Assertions.assertEquals(null, emitterRepository.findEmitterById(username));
    }


}
