package com.neoga.boltacution.exception.cotnroller;

import com.neoga.boltacution.exception.custom.CAuthEntryPointException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public void entrypointException() { throw new CAuthEntryPointException(); }

    @GetMapping(value = "/accessdenied")
    public void accessdeniedException() { throw new AccessDeniedException("");}
}