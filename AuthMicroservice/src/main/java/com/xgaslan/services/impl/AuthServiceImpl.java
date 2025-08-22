package com.xgaslan.services.impl;

import com.xgaslan.repositories.AuthRepository;
import com.xgaslan.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository repository;
}
