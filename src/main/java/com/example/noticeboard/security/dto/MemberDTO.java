package com.example.noticeboard.security.dto;

import com.example.noticeboard.entity.member.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MemberDTO implements UserDetails, OAuth2User {

    private String username;

    private String password;

    private String name;

    private String email;

    private Set<Role> RoleSet = new HashSet<>();

    private boolean fromSocial;

    private Map<String, Object> attr;

    @Override
    public Map<String, Object> getAttributes() {
        return attr;
    }

    @Override
    public String getName() { return name; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return RoleSet.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
