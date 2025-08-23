package com.xgaslan.services;

import com.xgaslan.documents.UserProfile;
import com.xgaslan.dtos.request.CreateUserRequestDto;

import java.util.List;

public interface UserProfileService {
    void create(CreateUserRequestDto dto);

    List<UserProfile> getAll();

    String upperName(String name);
}
