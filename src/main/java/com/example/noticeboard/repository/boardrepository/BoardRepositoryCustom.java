package com.example.noticeboard.repository.boardrepository;

import com.example.noticeboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {

    Page<Object[]> getBoardPage(String type, String keyword, String my, String username ,Pageable pageable);
    Optional<Board> getBoardWithAll(Long id);
    Optional<Board> getBoardWithImage(Long id);
}
