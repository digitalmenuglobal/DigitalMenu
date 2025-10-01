
package com.menu.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.menu.security.JwtService;
import com.menu.user.User;
import com.menu.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
    public static class RegisterResult {
        private final User user;
        public RegisterResult(User user) {
            this.user = user;
        }
        public User user() { return user; }
    }

    public JwtService getJwtService() {
        return jwtService;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public RegisterResult register(String email, String password, String confirmPassword, String restaurantName, String phoneNumber, String address, String cuisineType) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
    user.setRestaurantName(restaurantName);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        user.setCuisineType(cuisineType);
        userRepository.save(user);
    return new RegisterResult(user);
    }

    public String authenticate(String email, String password) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(token);
        return jwtService.generateToken(email);
    }
}



