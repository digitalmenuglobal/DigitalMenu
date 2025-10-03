
package com.menu.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.menu.security.JwtService;
import com.menu.user.User;
import com.menu.user.UserRepository;
import com.menu.util.EmailService;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
    // In-memory OTP storage: email -> OTP
    private final java.util.Map<String, String> otpStorage = new java.util.concurrent.ConcurrentHashMap<>();

    public void generateAndSendOtp(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000); // 6-digit OTP
        otpStorage.put(email, otp);
        String subject = "DigitalMenu Password Reset OTP";
        String text = "Your OTP for password reset is: " + otp + "\nThis OTP is valid for 10 minutes.";
        emailService.sendEmail(email, subject, text);
    }

    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpStorage.get(email);
        return storedOtp != null && storedOtp.equals(otp);
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
    }

    @Transactional
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    @Transactional
    public User updateUserDetails(String email, String restaurantName, String phoneNumber, String address, String cuisineType, String logo, String openingTime, String closingTime) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (restaurantName != null) user.setRestaurantName(restaurantName);
        if (phoneNumber != null) user.setPhoneNumber(phoneNumber);
        if (address != null) user.setAddress(address);
        if (cuisineType != null) user.setCuisineType(cuisineType);
        if (logo != null) user.setLogo(logo);
        if (openingTime != null) user.setOpeningTime(openingTime);
        if (closingTime != null) user.setClosingTime(closingTime);
        userRepository.save(user);
        return user;
    }
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
    private final EmailService emailService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    @Transactional
    public RegisterResult register(String email, String password, String confirmPassword, String restaurantName, String phoneNumber, String address, String cuisineType, String logo) {
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
        user.setLogo(logo);
        user.setVerified(false);
        userRepository.save(user);

        // Send verification email
        String verifyLink = "https://digitalmenu-psm5.onrender.com/api/auth/verify?userId=" + user.getId();
        String subject = "Verify your DigitalMenu account";
        String text = "Hello " + restaurantName + ",\n\nPlease verify your account by clicking the link below:\n" + verifyLink + "\n\nThank you!";
        emailService.sendEmail(email, subject, text);

        return new RegisterResult(user);
    }

    public String authenticate(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.isVerified()) {
            throw new IllegalArgumentException("Account not verified. Please check your email.");
        }
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(token);
        return jwtService.generateToken(email);
    }

    @Transactional
    public boolean verifyUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setVerified(true);
        userRepository.save(user);
        return true;
    }
}



