package com.neoga.autionnara.security;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SocialLoginTest  {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void contextLoads() {
    }
}
