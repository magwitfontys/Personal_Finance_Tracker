package com.example.backend.bll.port.out;

import com.example.backend.bll.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserInterface {
    Optional<UserDTO> findByUsername(String username);

    Optional<UserDTO> findById(Integer id);

    boolean existsByUsername(String username);

    UserDTO save(UserDTO user);

    boolean deleteById(Integer id);

    // counting the accounts
    long count();

    List<UserDTO> findAll();

}