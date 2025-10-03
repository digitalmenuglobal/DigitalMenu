
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
    public static record ForgotPasswordRequest(@Email String email) {}
    public static record VerifyOtpRequest(@Email String email, String otp) {}
    public static record UpdatePasswordRequest(@Email String email, String otp, String newPassword) {}

    @PostMapping("/forgot-password/request-otp")
    public ResponseEntity<String> requestOtp(@RequestBody ForgotPasswordRequest request) {
        try {
            authService.generateAndSendOtp(request.email());
            return ResponseEntity.ok("OTP sent to your email.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest request) {
        boolean valid = authService.verifyOtp(request.email(), request.otp());
        if (valid) {
            return ResponseEntity.ok("OTP verified. You can now reset your password.");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
    }

    @PostMapping("/forgot-password/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest request) {
        boolean valid = authService.verifyOtp(request.email(), request.otp());
        if (!valid) {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
        authService.updatePassword(request.email(), request.newPassword());
        authService.clearOtp(request.email());
        return ResponseEntity.ok("Password updated successfully.");
    }
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
        String logo,
        String openingTime,
        String closingTime
    ) {}

    @PostMapping("/update-user")
    public ResponseEntity<AuthResponse> updateUser(@RequestBody UpdateUserRequest request) {
        User user = authService.updateUserDetails(request.email(), request.restaurantName(), request.phoneNumber(), request.address(), request.cuisineType(), request.logo(), request.openingTime(), request.closingTime());
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
        String logo,
        String openingTime,
        String closingTime
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
        String openingTime,
        String closingTime,
        boolean isVerified,
        String token,
        String message
    ) {}


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            var result = authService.register(
                request.email(),
                request.password(),
                request.confirmPassword(),
                request.restaurantName(),
                request.phoneNumber(),
                request.address(),
                request.cuisineType(),
                request.logo(),
                request.openingTime(),
                request.closingTime()
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
                "Registration success. Please verify your email before logging in."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(
                null,
                request.email(),
                request.restaurantName(),
                request.phoneNumber(),
                request.address(),
                request.cuisineType(),
                request.logo(),
                null,
                e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new AuthResponse(
                null,
                request.email(),
                request.restaurantName(),
                request.phoneNumber(),
                request.address(),
                request.cuisineType(),
                request.logo(),
                null,
                "Registration failed: " + e.getMessage()
            ));
        }
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
            user.getOpeningTime(),
            user.getClosingTime(),
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
            user.getOpeningTime(),
            user.getClosingTime(),
            user.isVerified(),
            token,
            "user info"
        ));
    }

}



