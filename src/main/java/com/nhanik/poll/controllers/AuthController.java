package com.nhanik.poll.controllers;

import com.nhanik.poll.payload.AuthenticationRequest;
import com.nhanik.poll.payload.AuthenticationResponse;
import com.nhanik.poll.payload.RegistrationRequest;
import com.nhanik.poll.payload.Response;
import com.nhanik.poll.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest request) {
        authService.createNewUser(request);
        return ResponseEntity.ok(Response.withSuccessMsg("User registered"));
    }

    @PostMapping("login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authService.authenticateUser(request);
        return ResponseEntity.ok(Response.withData(response));
    }
}
