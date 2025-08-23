package com.xgaslan.services.impl;

import com.xgaslan.dtos.request.LoginRequestDto;
import com.xgaslan.dtos.request.RegisterRequestDto;
import com.xgaslan.dtos.request.feign.CreateUserRequestDto;
import com.xgaslan.entities.Auth;
import com.xgaslan.manager.UserProfileManager;
import com.xgaslan.repositories.AuthRepository;
import com.xgaslan.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository repository;
    private final UserProfileManager userProfileManager;

    // Need to handle those requests in a transaction. We can use eventual consistency for that.
    // Basic way to handle that situation, we can use Two-Phase Commit (2PC) pattern.
    // But 2PC is not suitable for high-throughput systems due to its blocking nature.
    // So we can use a Saga pattern for that. Saga pattern is a sequence of local transactions.
    // Each local transaction updates the database and publishes a message or event to trigger the next local transaction in the saga.
    // If a local transaction fails, the saga executes compensating transactions to undo the changes made by the preceding local transactions.
    // We can implement a Saga pattern using orchestration or choreography.
    // In orchestration, a central coordinator (orchestrator) tells the participants what local transactions to execute.
    // In choreography, each participant produces and listens to events and decides when to execute local transactions based on the events it receives.
    // We can use a message broker like RabbitMQ or Kafka to implement a Saga pattern.
    // But for simplicity, we will not implement a Saga pattern here. We will just call the user profile service after creating the auth.
    // Also, we can consider using an Outbox pattern to ensure that the messages are sent even if the service crashes after updating the database.
    // Outbox pattern is a way to ensure that messages are sent even if the service crashes after updating the database.
    // In Outbox pattern, we store the messages in a separate table (outbox table) in the same database as the main data.
    // A separate process (message relay) reads the messages from the outbox table and sends them to the message broker.
    // Once the message is sent successfully, the message relay deletes the message from the outbox table.
    // This way, we can ensure that the messages are sent even if the service crashes after updating the database.
    // Also, we can implement a retry mechanism in the message relay to handle transient failures. And circuit breaker pattern to handle permanent failures.
    // In a real-world application, we should handle the failure of user profile service and implement compensating transaction to delete the auth if user profile creation fails
    @Override
    public Auth register(RegisterRequestDto dto) {
        Auth auth = repository.save(Auth.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .email(dto.getEmail())
                .build());

        userProfileManager.create(CreateUserRequestDto.builder()
                        .authId(auth.getId().toString())
                        .username(auth.getUsername())
                        .email(auth.getEmail())
                .build());

        return auth;
    }

    @Override
    public Boolean login(LoginRequestDto dto) {
        return repository.existsByUsernameAndPassword(dto.getUsername(), dto.getPassword());
    }
}
