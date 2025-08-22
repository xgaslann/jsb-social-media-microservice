package com.xgaslan.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    @Id
    private String id;

    private String authId;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    Boolean isActive;

    Instant createdAt;
}
