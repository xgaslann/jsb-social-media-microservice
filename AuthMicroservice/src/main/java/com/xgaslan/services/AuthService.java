package com.xgaslan.services;

import com.xgaslan.dtos.request.LoginRequestDto;
import com.xgaslan.dtos.request.RegisterRequestDto;
import com.xgaslan.entities.Auth;

public interface AuthService {
    Auth register(RegisterRequestDto dto);

    Boolean login(LoginRequestDto dto);
}
