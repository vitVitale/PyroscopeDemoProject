package org.testing.pt.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testing.pt.model.User;
import org.testing.pt.repository.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Counter userCreatedCounter;

    @Autowired
    private Counter userRetrievedCounter;

    @Autowired
    private MeterRegistry meterRegistry;

    public Mono<User> createUser(User user) {
        long startTime = System.nanoTime();
        return userRepository.save(user)
                .doOnSuccess(savedUser -> {
                    Timer.builder("user_operation_duration")
                            .tag("operation", "create")
                            .register(meterRegistry)
                            .record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                    userCreatedCounter.increment();
                });
    }

    public Mono<User> getUserById(String id) {
        long startTime = System.nanoTime();
        return userRepository.findById(id)
                .doOnSuccess(user -> {
                    Timer.builder("user_operation_duration")
                            .tag("operation", "get_by_id")
                            .register(meterRegistry)
                            .record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
                    if (user != null) {
                        userRetrievedCounter.increment();
                    }
                });
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll()
                .doOnComplete(() -> userRetrievedCounter.increment());
    }
}