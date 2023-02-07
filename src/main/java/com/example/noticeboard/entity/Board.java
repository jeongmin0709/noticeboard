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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Image> imageList = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String content;

    private int recomendNum;

    private int viewNum;

    @Builder
    public Board(Long id, Member member, String title, String content, int recomendNum, int viewNum) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
        this.recomendNum = recomendNum;
        this.viewNum = viewNum;
    }


    public void setTitle(String title){this.title = title;}

    public void setContent(String content){this.content = content;}

    public void addRecommendNum(){ recomendNum++; }

    public void addViewNum(){viewNum++;}

    public void addImage(Image image){
        image.setBoard(this);
        imageList.add(image);
    }
    public void removeImage(){
        imageList.clear();
    }

}
