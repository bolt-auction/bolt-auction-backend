package com.neoga.boltauction.security.config;

import com.neoga.boltauction.security.service.JwtTokenService;
import com.neoga.boltauction.security.util.CustomAccessDeniedHandler;
import com.neoga.boltauction.security.util.CustomAuthEntryPoint;
import com.neoga.boltauction.security.util.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenService jwtTokenService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()    .disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/api/member/**").hasRole("USER")
                        .antMatchers("/api/chat/**").hasRole("USER")
                        .antMatchers("/api/auth/**","/api/social/**","/favicon.ico").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/**","/exception/**","/*").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/member/**", "/api/bid/**").permitAll()
                    .anyRequest().hasRole("USER")
                .and()
                    .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                    .exceptionHandling().authenticationEntryPoint(new CustomAuthEntryPoint())
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenService), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }
}