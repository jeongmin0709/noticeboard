package com.example.noticeboard.repository;

import com.example.noticeboard.entity.Board;
import com.example.noticeboard.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    void deleteByBoard(Board board);
}
