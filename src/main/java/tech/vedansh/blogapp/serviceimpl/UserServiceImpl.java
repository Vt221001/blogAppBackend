package tech.vedansh.blogapp.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.vedansh.blogapp.dto.ApiResponse;
import tech.vedansh.blogapp.dto.JwtAuthResponse;
import tech.vedansh.blogapp.dto.UserLoginRequest;
import tech.vedansh.blogapp.dto.UserRegisterRequest;
import tech.vedansh.blogapp.enums.Role;
import tech.vedansh.blogapp.repository.UserRepository;
import tech.vedansh.blogapp.service.UserService;
import tech.vedansh.blogapp.util.JwtUtil;
import tech.vedansh.blogapp.model.UserModel;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public ApiResponse<JwtAuthResponse> register(UserRegisterRequest request) {
        if (request.getEmail() == null || request.getPassword() == null || request.getName() == null) {
            throw new IllegalArgumentException("Name, email, and password are required");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        UserModel user = UserModel.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.ROLE_USER)
                .enabled(true)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        JwtAuthResponse jwtData = new JwtAuthResponse(token, user.getName());

        return new ApiResponse<>(true, "User registered successfully", jwtData);
    }


    @Override
    public ApiResponse<JwtAuthResponse> login(UserLoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        UserModel user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        JwtAuthResponse jwtData = new JwtAuthResponse(token, user.getName());

        return new ApiResponse<>(true, "Login successful", jwtData);
    }

    @Override
    public ApiResponse<List<UserModel>> getAll() {
        List<UserModel> users = userRepository.findAll();

        return new ApiResponse<>(
                true,
                users.isEmpty() ? "No users found" : "All users fetched",
                users.isEmpty() ? null : users
        );

    }

    public ApiResponse<UserModel> deleteUser(int id) {
        UserModel user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return new ApiResponse<>(false,"User not found with ID: " +id ,null);
        }

        userRepository.delete(user);

        return new ApiResponse<>(true,"User deleted successfully",user);

    }

    public ApiResponse<UserModel> getUser(int id) {

        UserModel user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return new ApiResponse<>(false,"User not found with ID: " +id ,null);
        }
        return new ApiResponse<>(true,"User fetched successfully",user);

    }

    @Override
    public ApiResponse<UserModel> updateUser(int id, UserModel updatedUser) {
        UserModel existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return new ApiResponse<>(false, "User not found with ID: " + id, null);
        }

        if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
            existingUser.setName(updatedUser.getName());
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
            if (userRepository.findByEmail(updatedUser.getEmail()).isPresent()
                    && !existingUser.getEmail().equals(updatedUser.getEmail())) {
                return new ApiResponse<>(false, "Email already in use", null);
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(existingUser);

        return new ApiResponse<>(true, "User updated successfully", existingUser);
    }



}



