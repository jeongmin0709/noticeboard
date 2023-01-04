package com.example.noticeboard.repository.boardrepository;

import com.example.noticeboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {

    Page<Object[]> getBoardPage(String type, String keyword, Pageable pageable);
    List<Object[]> getBoardWithAll(Long id);
}
