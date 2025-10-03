
package com.menu.auth;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.menu.user.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/auth")
@Validated
@CrossOrigin
public class AuthController {
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@org.springframework.web.bind.annotation.RequestParam Long userId) {
        boolean verified = authService.verifyUser(userId);
        if (verified) {
            return ResponseEntity.ok("Account verified successfully. You can now log in.");
        } else {
            return ResponseEntity.badRequest().body("Verification failed.");
        }
    }
    public static record UpdateUserRequest(
        @Email String email,
        String restaurantName,
        String phoneNumber,
        String address,
        String cuisineType,
        String logo
    ) {}

    @PostMapping("/update-user")
    public ResponseEntity<AuthResponse> updateUser(@RequestBody UpdateUserRequest request) {
        User user = authService.updateUserDetails(request.email(), request.restaurantName(), request.phoneNumber(), request.address(), request.cuisineType(), request.logo());
        return ResponseEntity.ok(new AuthResponse(
            user.getId(),
            user.getEmail(),
            user.getRestaurantName(),
            user.getPhoneNumber(),
            user.getAddress(),
            user.getCuisineType(),
            user.getLogo(),
            null,
            "User updated successfully"
        ));
    }

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    public static record RegisterRequest(
        @Email String email,
        @NotBlank String password,
        @NotBlank String confirmPassword,
        @NotBlank String restaurantName,
        @NotBlank String phoneNumber,
        @NotBlank String address,
        @NotBlank String cuisineType,
        String logo
    ) {}
    public static record AuthRequest(@Email String email, @NotBlank String password) {}
    public static record AuthResponse(
        Long id,
        String email,
        String restaurantName,
        String phoneNumber,
        String address,
        String cuisineType,
        String logo,
        String token,
        String message
    ) {}
    public static record LoginResponse(
        Long id,
        String email,
        String restaurantName,
        String phoneNumber,
        String address,
        String logo,
        boolean isVerified,
        String token,
        String message
    ) {}


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        var result = authService.register(
            request.email(),
            request.password(),
            request.confirmPassword(),
            request.restaurantName(),
            request.phoneNumber(),
            request.address(),
            request.cuisineType(),
            request.logo()
        );
        User user = result.user();
        return ResponseEntity.ok(new AuthResponse(
            user.getId(),
            user.getEmail(),
            user.getRestaurantName(),
            user.getPhoneNumber(),
            user.getAddress(),
            user.getCuisineType(),
            user.getLogo(),
            null,
            "Registration success"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest request) {
        String token = authService.authenticate(request.email(), request.password());
        User user = authService.getUserByEmail(request.email());
        return ResponseEntity.ok(new LoginResponse(
            user.getId(),
            user.getEmail(),
            user.getRestaurantName(),
            user.getPhoneNumber(),
            user.getAddress(),
            user.getLogo(),
            user.isVerified(),
            token,
            "login success"
        ));
    }
    @GetMapping("/me")
    public ResponseEntity<LoginResponse> getMe(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String token = authHeader.substring(7);
        String email = authService.getJwtService().extractRestaurantName(token);
        User user = authService.getUserByEmail(email);
        return ResponseEntity.ok(new LoginResponse(
            user.getId(),
            user.getEmail(),
            user.getRestaurantName(),
            user.getPhoneNumber(),
            user.getAddress(),
            user.getLogo(),
            user.isVerified(),
            token,
            "user info"
        ));
    }

}



