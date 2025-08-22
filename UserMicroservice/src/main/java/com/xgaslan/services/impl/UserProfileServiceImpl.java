package com.xgaslan.services.impl;

import com.xgaslan.documents.UserProfile;
import com.xgaslan.dtos.request.CreateUserRequestDto;
import com.xgaslan.repositories.UserProfileRepository;
import com.xgaslan.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository repository;


    @Override
    public void create(CreateUserRequestDto dto) {
        repository.save(UserProfile.builder()
                .authId(dto.getAuthId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .build());
    }

    @Override
    public List<UserProfile> getAll() {
        return repository.findAll();
    }
}
