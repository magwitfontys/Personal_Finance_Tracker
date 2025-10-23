package com.example.backend.bll.service;

import com.example.backend.bll.dto.LoginRequest;
import com.example.backend.bll.dto.LoginResult;
import com.example.backend.bll.dto.RegisterRequest;
import com.example.backend.bll.dto.UserDTO;

public interface AuthService {
    UserDTO register(RegisterRequest request);
    LoginResult login(LoginRequest request);
}
