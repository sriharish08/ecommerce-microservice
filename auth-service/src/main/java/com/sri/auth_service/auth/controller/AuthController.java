package com.sri.auth_service.auth.controller;

import com.sri.auth_service.auth.dto.request.LoginRequest;
import com.sri.auth_service.auth.dto.request.RegisterRequest;
import com.sri.auth_service.auth.dto.response.AuthResponse;
import com.sri.auth_service.auth.dto.response.LoginResponse;
import com.sri.auth_service.auth.service.AuthService;
import com.sri.auth_service.common.response.Response;
import com.sri.auth_service.common.response.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseBuilder.success(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseBuilder.success(authService.login(request));
    }
}
