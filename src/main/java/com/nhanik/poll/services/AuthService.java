package com.nhanik.poll.services;

import com.nhanik.poll.exception.RegistrationFailureException;
import com.nhanik.poll.models.User;
import com.nhanik.poll.payload.AuthenticationRequest;
import com.nhanik.poll.payload.AuthenticationResponse;
import com.nhanik.poll.payload.RegistrationRequest;
import com.nhanik.poll.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, JwtTokenService jwtTokenService, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public void createNewUser(RegistrationRequest request) {
        String email = request.email();
        String password = request.password();
        if (userRepository.existsByEmail(email)) {
            throw new RegistrationFailureException(email);
        }
        try {
            User user = new User();
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RegistrationFailureException(email);
        }
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        logger.info("Trying to authenticate ...");
        String email = request.email();
        String password = request.password();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails userDetails) {
                logger.info("Authentication successful!");
                String jwt = jwtTokenService.generateToken(userDetails);
                return new AuthenticationResponse(jwt);
            }
        }
        return new AuthenticationResponse("");
    }
}
