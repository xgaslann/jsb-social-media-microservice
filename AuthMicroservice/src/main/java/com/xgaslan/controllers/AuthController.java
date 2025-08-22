package com.xgaslan.controllers;

import com.xgaslan.dtos.request.LoginRequestDto;
import com.xgaslan.dtos.request.RegisterRequestDto;
import com.xgaslan.entities.Auth;
import com.xgaslan.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.xgaslan.config.RestApis.*;

@RestController
@RequestMapping(AUTH_SERVICE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping(REGISTER)
    public ResponseEntity<Auth> register(@RequestBody RegisterRequestDto dto){
        if (!dto.getPassword().equals(dto.getRePassword()))
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(service.register(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<Boolean> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(service.login(dto));
    }
}
