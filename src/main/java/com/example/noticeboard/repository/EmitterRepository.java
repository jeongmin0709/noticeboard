package com.example.noticeboard.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter);

    Map<String, SseEmitter> findAllStartWithByUsername(String username);

    void deleteAllStartWithByUsername(String username);

    void deleteById(String id);

}
