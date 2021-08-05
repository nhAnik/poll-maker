package com.nhanik.poll.services;

import com.nhanik.poll.models.User;
import com.nhanik.poll.payload.AuthenticationRequest;
import com.nhanik.poll.payload.RegistrationRequest;
import com.nhanik.poll.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    public User findById(Long uid) {
        return userRepository.findById(uid)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    public void createNewUser(RegistrationRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new IllegalStateException("User already registered");
                });
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public String authenticateUser(AuthenticationRequest request) {
        logger.info("Trying to authenticate ...");
        String email = request.getEmail();
        String password = request.getPassword();
        Authentication authentication = null;

        try {
            authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(email, password)
                    );
        } catch (AuthenticationException e) {
            logger.error(e.getMessage());
        }

        if (authentication == null) {
            return "";
        }
        // (todo) need to cast
        logger.info("Authentication successful!");

        UserDetails userDetails = loadUserByUsername(email);
        String jwt = jwtTokenService.generateToken(userDetails);
        return jwt;
    }
}
