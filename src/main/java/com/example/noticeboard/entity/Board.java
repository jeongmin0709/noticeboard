package com.example.noticeboard.entity;

import com.example.noticeboard.entity.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Board extends BaseEntity{

    @Id
    @Column(name = "BOARD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    @ToString.Exclude
    private Member member;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int recomendNum;

    private int viewNum;


    public void changeTitle(String title){this.title = title;}

    public void changeContent(String content){this.content = content;}

    public void addRecommendNum(){ recomendNum++; }

    public void addViewNum(){viewNum++;}

}
