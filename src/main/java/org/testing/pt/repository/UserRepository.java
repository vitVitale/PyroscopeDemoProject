package org.testing.pt.repository;

import org.springframework.stereotype.Repository;
import org.testing.pt.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public Mono<User> save(User user) {
        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }
        users.put(user.getId(), user);
        return Mono.just(user);
    }

    public Mono<User> findById(String id) {
        return Mono.justOrEmpty(users.get(id));
    }

    public Flux<User> findAll() {
        return Flux.fromIterable(users.values());
    }
}