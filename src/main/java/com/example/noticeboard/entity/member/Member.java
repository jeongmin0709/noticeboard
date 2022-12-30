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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Builder
public class Member extends BaseEntity {

    @Id
    private String username;

    private String password;

    private String email;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Set<Role> roleSet = new HashSet<>();

    private boolean fromSocial;


    public void changePassword(String password){this.password = password;}
    public void addRole(Role role){roleSet.add(role);}
    public void removeRole(Role role){roleSet.remove(role);}

}
