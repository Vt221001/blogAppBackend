package tech.vedansh.blogapp.service;

import org.springframework.stereotype.Service;
import tech.vedansh.blogapp.dto.ApiResponse;
import tech.vedansh.blogapp.dto.JwtAuthResponse;
import tech.vedansh.blogapp.dto.UserLoginRequest;
import tech.vedansh.blogapp.dto.UserRegisterRequest;


public interface UserService {
    ApiResponse<JwtAuthResponse> register(UserRegisterRequest request);
    ApiResponse<JwtAuthResponse> login(UserLoginRequest request);
}
