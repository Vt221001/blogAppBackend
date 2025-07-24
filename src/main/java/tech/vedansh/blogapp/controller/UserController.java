package tech.vedansh.blogapp.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tech.vedansh.blogapp.dto.ApiResponse;
import tech.vedansh.blogapp.dto.JwtAuthResponse;
import tech.vedansh.blogapp.dto.UserLoginRequest;
import tech.vedansh.blogapp.dto.UserRegisterRequest;
import tech.vedansh.blogapp.model.UserModel;
import tech.vedansh.blogapp.serviceimpl.UserServiceImpl;

import java.util.List;

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


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserModel>>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }


    @PreAuthorize(("hasRole('ADMIN')"))
    @DeleteMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserModel>> deleteUser(@PathVariable int id) {
        return ResponseEntity.ok(userService.deleteUser(id));

    }


    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserModel>> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.getUser(id));
    }


    @PutMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserModel>> updateUser(@PathVariable int id, @RequestBody UserModel userModel) {
        return ResponseEntity.ok(userService.updateUser(id,userModel));
    }





}
