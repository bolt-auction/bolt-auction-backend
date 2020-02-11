package com.neoga.boltacution.security.service;

import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.security.dto.LoginInfo;
import com.neoga.boltacution.security.dto.LoginUserDto;
import com.neoga.boltacution.security.util.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Service
public class JwtTokenService {
    @Value("${spring.jwt.secret}")
    private String secretKey;
    private long tokenValidMilisecond = 1000L * 60 * 60;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 토큰 생성
    public String createToken(Members member) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(member.getId()));
        claims.put("name", member.getName());
        claims.put("email", member.getEmail());
        claims.put("roles", member.getRole());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) {
        Map<String, Object> parseInfo = getUserInfo(token);

        Long id = Long.parseLong((String)parseInfo.get("id"));
        Collection<? extends GrantedAuthority> roleList = SecurityUtil.authorities((List)parseInfo.get("authorities"));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(id, "", roleList);
        authentication.setDetails(parseInfo);
        return authentication;
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public Map<String, Object> getUserInfo(String token) {
        Jws<Claims> parseInfo = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        Map<String, Object> result = new HashMap<>();
        result.put("id", parseInfo.getBody().getSubject());
        result.put("name", parseInfo.getBody().get("name"));
        result.put("email", parseInfo.getBody().get("email"));
        result.put("authorities", parseInfo.getBody().get("roles", List.class));
        return result;
    }

    // Request의 Header에서 token 파싱 : "Bearer: jwt토큰"
    public String resolveToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer "))
            return null;
        String authToken = header.substring(7);

        return authToken;
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
