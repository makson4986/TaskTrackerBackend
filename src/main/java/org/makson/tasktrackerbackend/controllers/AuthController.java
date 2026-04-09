package org.makson.tasktrackerbackend.controllers;

import lombok.RequiredArgsConstructor;
import org.makson.tasktrackerbackend.dto.AuthDto;
import org.makson.tasktrackerbackend.mappers.UserMapper;
import org.makson.tasktrackerbackend.models.User;
import org.makson.tasktrackerbackend.services.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping("/user")
    public ResponseEntity<?> register(@RequestBody @Validated AuthDto authDto) {
        String jwt = authService.register(authDto);
        HttpHeaders headers = createHeaders(jwt);
        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Validated AuthDto authDto) {
        String jwt = authService.login(authDto);
        HttpHeaders headers = createHeaders(jwt);
        return ResponseEntity.ok().headers(headers).build();
    }

    private HttpHeaders createHeaders(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);
        return headers;
    }
}
