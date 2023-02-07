package com.example.noticeboard.entity.member;


import com.example.noticeboard.entity.BaseEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    private String username;

    private String password;

    private String name;

    @Column(unique = true)
    private String email;

    @ElementCollection(fetch = FetchType.LAZY)
    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    private Set<Role> roleSet = new HashSet<>();

    private boolean fromSocial;

    @Builder
    public Member(String username, String password, String name, String email, boolean fromSocial) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.fromSocial = fromSocial;
    }


    public void setPassword(String password){this.password = password;}
    public void setEmail(String email){this.email = email;}
    public void addRole(Role role){roleSet.add(role);}
    public void removeRole(Role role){roleSet.remove(role);}

}
