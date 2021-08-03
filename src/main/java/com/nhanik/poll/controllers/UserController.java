package com.nhanik.poll.controllers;

import com.nhanik.poll.models.User;
import com.nhanik.poll.payload.AuthenticationRequest;
import com.nhanik.poll.payload.AuthenticationResponse;
import com.nhanik.poll.payload.RegistrationRequest;
import com.nhanik.poll.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<?> createUser(@RequestBody RegistrationRequest request) {
        userService.createNewUser(request);
        return ResponseEntity.ok("User created");
    }

    @PostMapping("login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        logger.info("Auth request");
        String jwt = userService.authenticateUser(request);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
