package com.example.noticeboard.security.filter;

import com.example.noticeboard.entity.member.Role;
import com.example.noticeboard.security.dto.MemberDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    @Value("${jwt.secret}") private String secretKey;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("인증 요청");
        String jwtToken = resolveJwtToken(request);
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        if(jwtToken == null || !validationToken(jwtToken, key)) {
            chain.doFilter(request, response);
            return;
        }
        Authentication authentication = getAuthentication(jwtToken,key);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private String resolveJwtToken(HttpServletRequest request){
        String jwtHeader= request.getHeader("Authorization");
        if(jwtHeader == null || !jwtHeader.startsWith("Bearer")) return null;
        return jwtHeader.replace("Bearer ", "");
    }

    private boolean validationToken(String jwtToken, Key key){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key).build().parseClaimsJws(jwtToken);
            return true;
        }
        catch (SecurityException | MalformedJwtException e){
            log.warn("유효하지 않은 JWT 토큰입니다.", e);
        }catch (ExpiredJwtException e){
            log.warn("만료된 JWT 토큰입니다.", e);
        }catch (UnsupportedJwtException e){
            log.warn("지원하지 않는 JWT 토큰입니다.", e);
        }catch (IllegalArgumentException e){
            log.warn("JWT토큰이 잘못되었습니다.", e);
        }
        return false;
    }
    public Authentication getAuthentication(String jwtToken, Key key) {
        // 토큰 복호화
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build().parseClaimsJws(jwtToken).getBody();

        if (claims.get("roles") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        // 클레임에서 권한 정보 가져오기
        Set<Role> roleSet = Arrays.stream(claims.get("roles").toString().replaceAll("\\[|\\]", "").split(","))
                        .map(role->Role.valueOf(role)).collect(Collectors.toSet());
        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = MemberDTO.builder().username(claims.getSubject()).RoleSet(roleSet).build();
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

}
