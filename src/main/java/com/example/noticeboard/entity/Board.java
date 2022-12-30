package com.example.noticeboard.entity;

import com.example.noticeboard.entity.member.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString(exclude = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity{

    @Id
    @Column(name = "BOARD_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int recomendNum;

    private int viewNum;


    @Builder
    public Board(Member member, String title, String content) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.recomendNum = 0;
        this.viewNum = 0;
    }

    public void changeTitle(String title){this.title = title;}
    public void changeContent(String content){this.content = content;}
    public void addRecommendNum(){ recomendNum++; }
    public void addViewNum(){viewNum++;}
    public void setId(Long id){this.id = id;}// 테스트용
}
