package com.example.noticeboard.entity;

import com.example.noticeboard.entity.member.Member;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@BatchSize(size=100)
@Builder
public class Comment extends BaseEntity{

    @Id
    @Column(name = "COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    @ToString.Exclude
    private Comment parent;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", nullable = false)
    @ToString.Exclude
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    @ToString.Exclude
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int recomendNum;


    public void setParent(Comment comment){
        this.parent = comment;
    }
    public void changeContent(String content){this.content = content;}
    public void addRecomendNum(){recomendNum++;}
}
