package com.example.noticeboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@AllArgsConstructor
@Builder
public class PageResultDTO<DTO, EN> {
    //DTO리스트
    @ToString.Exclude
    private List<DTO> dtoList;
    //총 페이지 번호
    private int totalPage;
    // 현재 페이지 번호
    private int page;
    // 목록 사이즈
    private int size;
    // 시작페이지 번호, 끝페이지 번호
    private int start, end;
    //이전, 다음
    private boolean prev, next;
    //페이지 번호 목록
    private List<Integer> pageList;

    public PageResultDTO(Page<DTO> result){
        dtoList = result.stream().collect(Collectors.toList());
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber()+1; // 0부터 시작하기떄문에 1더함
        this.size = pageable.getPageSize();
        //임시의 마지막 페이지 계산
        int tempEnd = (int)(Math.ceil(page/10.0))*10;
        start = tempEnd - 9;
        prev = start > 1;
        // 전체 페이지가 임시마지막페이지보다 크다면 임시마지막페이지, 작거나 같다면 전체 페이지가 끝페이지
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
