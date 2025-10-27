package com.example.backend.dal.adapter;

import com.example.backend.bll.dto.UserDTO;
import com.example.backend.bll.port.out.UserInterface;
import com.example.backend.dal.entity.UserEntity;
import org.springframework.dao.DataIntegrityViolationException;
import com.example.backend.dal.repo.UserJpaRepository;
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

    // read

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

    // write

    @Override
    @Transactional
    public UserDTO save(UserDTO user) {
        try {
            UserEntity saved;
            if (user.getId() == null) {
                // CREATE
                UserEntity e = new UserEntity();
                e.setUsername(user.getUsername());
                e.setPasswordHash(user.getPasswordHash()); // BLL already hashed
                // createdAt: let @PrePersist set it, but set if provided
                if (user.getCreatedAt() != null) {
                    e.setCreatedAt(user.getCreatedAt());
                }
                saved = repo.save(e);
            } else {
                // UPDATE (load, modify, save)
                UserEntity e = repo.findById(user.getId())
                        .orElseThrow(() -> new IllegalArgumentException("User not found: id=" + user.getId()));
                e.setUsername(user.getUsername());
                // only replace hash if BLL provided one (avoid nuking existing hash)
                if (user.getPasswordHash() != null && !user.getPasswordHash().isBlank()) {
                    e.setPasswordHash(user.getPasswordHash());
                }
                // createdAt stays unchanged (column is updatable=false in your entity)
                saved = repo.save(e);
            }
            return toDTO(saved);
        } catch (DataIntegrityViolationException ex) {
            // in case of race conditions on unique username
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

    // map

    private UserDTO toDTO(UserEntity e) {
        var d = new UserDTO();
        d.setId(e.getId());
        d.setUsername(e.getUsername());
        d.setPasswordHash(e.getPasswordHash()); // marked @JsonIgnore in DTO
        d.setCreatedAt(e.getCreatedAt());
        return d;
    }
}
