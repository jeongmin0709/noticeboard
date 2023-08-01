package com.example.noticeboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
public class SliceResultDTO<DTO, EN> {
    //DTO리스트
    private List<DTO> dtoList;

    private int size;

    private boolean hasNext;

    public SliceResultDTO(Slice<EN> result, Function<EN, DTO> fn){
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        size = result.getNumberOfElements();
        hasNext = result.hasNext();
    }
}
