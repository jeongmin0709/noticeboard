package com.example.noticeboard.entity.member;


import com.example.noticeboard.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name = "MEMBER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String email;

    private String name;

    private LocalDate birth_date;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private boolean fromSocial;


    @Builder
    public Member(String nickname, String email, String name, LocalDate birth_date, Gender gender, Role role, boolean fromSocial) {
        this.nickname = nickname;
        this.email = email;
        this.name = name;
        this.birth_date = birth_date;
        this.gender = gender;
        this.role = role;
        this.fromSocial = fromSocial;
    }

    public void changeNickname(String nickname){this.nickname = nickname;}
    public void changeEmail(String email){this.email = email;}
    public void changeRole(Role role){this.role = role;}
    public void setId(Long id){this.id = id;}//테스트용
}
