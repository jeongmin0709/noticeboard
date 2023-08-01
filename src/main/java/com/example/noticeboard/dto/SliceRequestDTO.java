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
public class SliceRequestDTO{

    private int page;
    private int size;
    private String order;
    private boolean isAsc;

    public SliceRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable() {
        if(StringUtils.hasText(order)) {
            return (isAsc) ? PageRequest.of(page-1, size, Sort.by(order).ascending().and(Sort.by("id").descending()))
                    : PageRequest.of(page-1, size, Sort.by(order).descending().and(Sort.by("id").descending()));
        }
        return (isAsc) ? PageRequest.of(page-1, size, Sort.by("id").ascending())
                : PageRequest.of(page-1, size, Sort.by("id").descending());
    }
}
