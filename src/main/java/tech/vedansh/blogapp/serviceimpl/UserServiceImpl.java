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

        String token = jwtUtil.generateToken(user.getName());

        JwtAuthResponse jwtData = new JwtAuthResponse(token, user.getName());

        return new ApiResponse<>(true, "Login successful", jwtData);
    }

}

