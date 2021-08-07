package com.nhanik.poll.services;

import com.nhanik.poll.exception.RegistrationFailureException;
import com.nhanik.poll.models.User;
import com.nhanik.poll.payload.AuthenticationRequest;
import com.nhanik.poll.payload.AuthenticationResponse;
import com.nhanik.poll.payload.RegistrationRequest;
import com.nhanik.poll.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    public void createNewUser(RegistrationRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        userRepository.findByEmail(email)
                .ifPresent(user -> {
                    throw new RegistrationFailureException(email);
                });
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        logger.info("Trying to authenticate ...");
        String email = request.getEmail();
        String password = request.getPassword();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        logger.info("Authentication successful!");

        // (todo) need to cast

        UserDetails userDetails = loadUserByUsername(email);
        String jwt = jwtTokenService.generateToken(userDetails);
        return new AuthenticationResponse(jwt);
    }
}
