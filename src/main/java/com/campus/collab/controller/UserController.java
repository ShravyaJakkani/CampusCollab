package com.campus.collab.controller;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/home")
    public String home() {
        return "Welcome";
    }

    @GetMapping("/test")
    public String test(Authentication authentication) {
        return "Hello " + authentication.getName();
    }

    @GetMapping("/whoami")
public String whoami(Authentication authentication) {
    if (authentication == null) {
        return "NULL AUTH";
    }

    return authentication.getName() + " | " + authentication.getAuthorities();
}

@GetMapping("/authorities")
public String authorities(Authentication auth) {
    return auth.getAuthorities().toString();
}
}