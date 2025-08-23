package com.xgaslan.services.impl;

import com.xgaslan.documents.UserProfile;
import com.xgaslan.dtos.request.CreateUserRequestDto;
import com.xgaslan.repositories.UserProfileRepository;
import com.xgaslan.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository repository;
    private final CacheManager cacheManager;

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

    @Override
    @Cacheable("upper-name-cache")
    public String upperName(String name) {
        String result = name.toUpperCase();
        try{
            Thread.sleep(3000L);
        }catch (Exception e){

        }
        return result;
    }

    public void clearCache(){
        Objects.requireNonNull(cacheManager.getCache("upper-name-cache")).clear();
    }
}
