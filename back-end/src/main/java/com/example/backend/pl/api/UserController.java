package com.example.backend.pl.api;

import com.example.backend.dal.entity.UserEntity;
import com.example.backend.dal.repo.UserJpaRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserJpaRepository users;

    public UserController(UserJpaRepository users) {
        this.users = users;
    }

    /** Read all users (for the smoke test) */
    @GetMapping
    public List<UserEntity> getAll() {
        return users.findAll();
    }

    /** Read one user by id */
    @GetMapping("/{id}")
    public UserEntity getOne(@PathVariable Integer id) {
        return users.findById(id).orElseThrow();
    }

    /**
     * OPTIONAL: Seed a single test user if it does not exist.
     * Call once, then use GET to verify rows are returned.
     */
    @PostMapping("/_seed")
    public UserEntity seed() {
        final String name = "smoke_user";
        return users.findByUsername(name)
                .orElseGet(() -> users.save(new UserEntity(
                        name,
                        "NOT_A_REAL_HASH", // just a placeholder for smoke test
                        LocalDateTime.now())));
    }
}
