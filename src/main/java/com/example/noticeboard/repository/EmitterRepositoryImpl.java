package com.example.noticeboard.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }


    @Override
    public Map<String, SseEmitter> findAllStartWithByUsername(String username) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(username))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void deleteAllStartWithByUsername(String username) {
        emitters.forEach((key, emitter) -> {
            if (key.startsWith(username)) emitters.remove(key);
        });
    }
    public void deleteById(String id){
        emitters.remove(id);
    }
}
