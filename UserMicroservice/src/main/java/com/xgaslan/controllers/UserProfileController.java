package com.xgaslan.controllers;

import com.xgaslan.documents.UserProfile;
import com.xgaslan.dtos.request.CreateUserRequestDto;
import com.xgaslan.services.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.xgaslan.configs.RestApis.*;

@RestController
@RequestMapping(USER_PROFILE_SERVICE)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;

    @PostMapping
    public ResponseEntity<Boolean> create(@RequestBody CreateUserRequestDto dto){
        service.create(dto);
        return ResponseEntity.ok(true);
    }

    @GetMapping
    public ResponseEntity<List<UserProfile>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }
}
