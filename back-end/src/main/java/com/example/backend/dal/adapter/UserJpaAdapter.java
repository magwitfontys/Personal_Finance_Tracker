package com.example.backend.dal.adapter;

import com.example.backend.bll.dto.UserDTO;
import com.example.backend.bll.port.out.UserInterface;
import com.example.backend.dal.entity.UserEntity;
import com.example.backend.dal.repo.UserJpaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UserJpaAdapter implements UserInterface {

    private final UserJpaRepository repo;

    public UserJpaAdapter(UserJpaRepository repo) {
        this.repo = repo;
    }

    // -------------------- READ --------------------

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findById(Integer id) {
        return repo.findById(id).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByUsername(String username) {
        return repo.findByUsername(username).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return repo.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repo.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return repo.findAll().stream().map(this::toDTO).toList();
    }

    // -------------------- WRITE --------------------

    @Override
    @Transactional
    public UserDTO save(UserDTO user) {
        try {
            UserEntity saved;

            if (user.getId() == null) {
                // CREATE
                UserEntity e = toEntity(user);
                // createdAt is set by @PrePersist if null
                saved = repo.save(e);
            } else {
                // UPDATE
                UserEntity e = repo.findById(user.getId())
                        .orElseThrow(() -> new IllegalArgumentException("User not found: id=" + user.getId()));

                e.setUsername(user.getUsername());

                // Only overwrite hash if provided (prevents wiping an existing hash)
                if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank()) {
                    e.setPasswordHash(user.getPasswordHash());
                }

                // createdAt remains unchanged (column is updatable=false in the entity)
                saved = repo.save(e);
            }

            return toDTO(saved);
        } catch (DataIntegrityViolationException ex) {
            // Handles race on unique username at DB level
            throw new IllegalStateException("Username already exists", ex);
        }
    }

    @Override
    @Transactional
    public boolean deleteById(Integer id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }

    // -------------------- MAPPING --------------------

    private UserDTO toDTO(UserEntity e) {
        // IMPORTANT: include passwordHash so AuthService.login() can verify
        UserDTO d = new UserDTO();
        d.setId(e.getId());
        d.setUsername(e.getUsername());
        d.setPasswordHash(e.getPasswordHash());
        d.setCreatedAt(e.getCreatedAt());
        return d;
    }

    private UserEntity toEntity(UserDTO d) {
        UserEntity e = new UserEntity();
        e.setId(d.getId());
        e.setUsername(d.getUsername());
        e.setPasswordHash(d.getPasswordHash()); // BLL passes a bcrypt hash on register
        e.setCreatedAt(d.getCreatedAt()); // may be null; @PrePersist will fill
        return e;
    }
}
