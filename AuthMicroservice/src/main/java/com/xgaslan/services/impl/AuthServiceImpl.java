package com.xgaslan.services.impl;

import com.xgaslan.dtos.request.LoginRequestDto;
import com.xgaslan.dtos.request.RegisterRequestDto;
import com.xgaslan.entities.Auth;
import com.xgaslan.repositories.AuthRepository;
import com.xgaslan.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository repository;

    @Override
    public Auth register(RegisterRequestDto dto) {
        return repository.save(Auth.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .build());
    }

    @Override
    public Boolean login(LoginRequestDto dto) {
        return repository.existsByUsernameAndPassword(dto.getUsername(), dto.getPassword());
    }
}
