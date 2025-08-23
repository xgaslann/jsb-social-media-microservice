package com.xgaslan.manager;

import com.xgaslan.dtos.request.feign.CreateUserRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:9091/api/dev/v1/user-profile", name = "userProfileManager")
public interface UserProfileManager {
    @PostMapping
    ResponseEntity<Boolean> create(@RequestBody CreateUserRequestDto dto);
}
