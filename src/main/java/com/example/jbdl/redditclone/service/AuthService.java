package com.example.jbdl.redditclone.service;

import com.example.jbdl.redditclone.dto.AuthenticationResponse;
import com.example.jbdl.redditclone.dto.LoginRequest;
import com.example.jbdl.redditclone.dto.RegisterRequest;
import com.example.jbdl.redditclone.exception.SpringRedditException;
import com.example.jbdl.redditclone.model.NotificationEmail;
import com.example.jbdl.redditclone.model.User;
import com.example.jbdl.redditclone.model.VerificationToken;
import com.example.jbdl.redditclone.repository.UserRepository;
import com.example.jbdl.redditclone.repository.VerificationTokenRepository;
import com.example.jbdl.redditclone.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;


    @Transactional
    public void signup(RegisterRequest registerRequest) {

        // Map RegisterRequest to user object (use model mapper later on)

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false); // User is enabled only after email verification

        // store it in the db
        userRepository.save(user);

        // Generate verification token to verify email after storing user in db
        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Please activate your account", user.getEmail(),
                "Thank you for signing up to Spring Reddit, " + "please click on the below url " +
                        "to activate your account: " +
                "http://localhost:8080/api/auth/accountVerification/" + token));

    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        // Store verification token in db,
        // So that if user decides to verify his account after 2-3 days the token persists
        verificationTokenRepository.save(verificationToken);

        return token;
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new SpringRedditException("Invalid token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
            new SpringRedditException("User not found with username " + username)
        );
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        // Implement the logic to authenticate user
        /*
        * Create UsernamePasswordAuthenticationToken
        * Use authenticationManager to perform login
        * */
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword())
        );


        SecurityContextHolder.getContext().setAuthentication(authenticate);

        // Create JWTs and send it as a response to the client
        String token = jwtProvider.generateToken(authenticate);

        // Send it back to the client using DTO
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }
}
