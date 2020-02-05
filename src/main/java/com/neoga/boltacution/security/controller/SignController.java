package com.neoga.boltacution.security.controller;

import com.neoga.boltacution.exception.CEmailLoginFailedException;
import com.neoga.boltacution.memberstore.member.domain.Members;
import com.neoga.boltacution.memberstore.member.domain.Role;
import com.neoga.boltacution.memberstore.member.repository.MemberRepository;
import com.neoga.boltacution.security.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class SignController {

    private final MemberRepository memberRepo;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/login")
    public ResponseEntity login(@RequestParam String email, @RequestParam String passwd) {
        Members member = memberRepo.findByEmail(email).orElseThrow(CEmailLoginFailedException::new);
        if (!passwordEncoder.matches(passwd, member.getPasswd()))
            throw new CEmailLoginFailedException();
        String token = jwtTokenService.createToken(member.getEmail(), member.getRole());
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity signin(@RequestParam String email, @RequestParam String passwd,  @RequestParam String name) {
        memberRepo.save(Members.builder()
                .email(email)
                .passwd(passwordEncoder.encode(passwd))
                .name(name)
                .role(Collections.singletonList(Role.USER))
                .build());
        return ResponseEntity.ok().build();
    }
}
