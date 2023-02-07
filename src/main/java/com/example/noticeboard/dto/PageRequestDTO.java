package com.example.noticeboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
@Builder
public class PageRequestDTO {
    private int page;
    private int size;
    private String type;
    private String keyword;
    private String order;
    private String my;

    public PageRequestDTO(){
        this.page = 1;
        this.size = 20;
    }

    public Pageable getPageable()
    {
        return (StringUtils.hasText(order))?
                PageRequest.of(page-1, size, Sort.by(order).descending().and(Sort.by("id").descending()))
                :PageRequest.of(page-1, size, Sort.by("id").descending());
    }
}
