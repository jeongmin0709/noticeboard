package com.example.noticeboard.repository.boardrepository;

import com.example.noticeboard.dto.PagingBoardDTO;
import com.example.noticeboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface BoardRepositoryCustom{

    Page<PagingBoardDTO> getBoardPage(String type, String keyword, String my, String username, Pageable pageable);
    Map<String, Object> getBoard(Long id);
    Optional<Board> getBoardWithImage(Long id);
    Map<String,Board> getPrevAndNextBoard(Long id);
}
