package com.example.noticeboard.service;

import com.example.noticeboard.dto.BoardDTO;
import com.example.noticeboard.dto.PageRequestDTO;
import com.example.noticeboard.dto.PageResultDTO;
import com.example.noticeboard.entity.Board;
import com.example.noticeboard.repository.boardrepository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {

        Function<Object[], BoardDTO> fn = (en -> entityToDto((Board)en[0], (Long)en[1]));
        Page<Object[]> result = boardRepository.getBoardPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("id").descending()));
        log.info(result.getTotalPages());
        return new PageResultDTO<>(result, fn);
    }
}
