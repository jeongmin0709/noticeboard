package com.example.noticeboard.sercurity;

import com.example.noticeboard.eannotation.WithMockCustomUser;
import com.example.noticeboard.entity.member.Role;
import com.example.noticeboard.security.dto.MemberDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {

        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Set<Role> roleSet = Arrays.stream(customUser.roles()).map(role->Role.valueOf(role)).collect(Collectors.toSet());
        UserDetails principal = MemberDTO.builder().username(customUser.username()).password(customUser.password()).RoleSet(roleSet).build();
        final UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());

        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
