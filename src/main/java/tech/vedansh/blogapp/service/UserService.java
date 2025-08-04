package tech.vedansh.blogapp.service;

import org.springframework.stereotype.Service;
import tech.vedansh.blogapp.dto.ApiResponse;
import tech.vedansh.blogapp.dto.JwtAuthResponse;
import tech.vedansh.blogapp.dto.UserLoginRequest;
import tech.vedansh.blogapp.dto.UserRegisterRequest;
import tech.vedansh.blogapp.model.UserModel;

import java.util.List;


public interface UserService {
    ApiResponse<JwtAuthResponse> register(UserRegisterRequest request);
    ApiResponse<JwtAuthResponse> login(UserLoginRequest request);
    ApiResponse<List<UserModel>> getAll();
    ApiResponse<UserModel> deleteUser(Long id);
    ApiResponse<UserModel> getUser(Long id);
    ApiResponse<UserModel> updateUser(Long id, UserModel userModel);
}

