package com.immx.industrialsupport.supportservice.services.authorization;

import com.immx.industrialsupport.contracts.authorization.LoginRequest;
import com.immx.industrialsupport.contracts.authorization.LoginResponse;
import com.immx.industrialsupport.supportservice.entities.User;
import com.immx.industrialsupport.supportservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис авторизации.
 */
@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public LoginResponse login(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByDepartment_IdAndUsernameIgnoreCase(
                loginRequest.getDepartmentId(),
                loginRequest.getUsername());

        if(userOptional.isEmpty())
            throw new BadCredentialsException("Invalid username or password");

        User user = userOptional.get();

        if(!user.isEnabled())
            throw new DisabledException("User is disabled");

        if(!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPasswordHash()))
            throw new BadCredentialsException("Invalid username or password");

        String accessToken = jwtService.generateAccessToken(user);

        return new LoginResponse(
                accessToken,
                "Bearer");
    }
}
