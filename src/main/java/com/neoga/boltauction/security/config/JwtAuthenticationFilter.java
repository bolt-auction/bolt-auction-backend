package com.neoga.boltauction.security.config;

import com.neoga.boltauction.exception.custom.CJwtTokenMissingException;
import com.neoga.boltauction.security.service.JwtTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class JwtAuthenticationFilter extends GenericFilterBean {

    private JwtTokenService jwtTokenService;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService){
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException, CJwtTokenMissingException {
        String token = jwtTokenService.resolveToken((HttpServletRequest) request);
        if (token != null & jwtTokenService.validateToken(token)) {
            Authentication auth = jwtTokenService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}