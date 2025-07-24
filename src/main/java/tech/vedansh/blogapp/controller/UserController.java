package tech.vedansh.blogapp.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.vedansh.blogapp.dto.ApiResponse;
import tech.vedansh.blogapp.dto.JwtAuthResponse;
import tech.vedansh.blogapp.dto.UserLoginRequest;
import tech.vedansh.blogapp.dto.UserRegisterRequest;
import tech.vedansh.blogapp.model.UserModel;
import tech.vedansh.blogapp.serviceimpl.UserServiceImpl;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> register(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }



    @GetMapping("/user")
    public String getAllUser(){
        return "all user is their";
    }

}
