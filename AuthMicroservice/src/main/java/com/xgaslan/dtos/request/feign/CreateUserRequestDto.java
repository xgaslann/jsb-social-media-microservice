package com.xgaslan.dtos.request.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequestDto {
    private String authId;

    private String username;

    private String email;
}
