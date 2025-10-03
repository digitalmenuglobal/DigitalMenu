package com.menu.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.menu.auth.AuthService;
import com.menu.user.User;
import com.menu.user.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@CrossOrigin
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String sub = oAuth2User.getAttribute("sub");

        // Find or create user
        Optional<User> userOpt = userRepository.findByEmail(email);
        User user = userOpt.orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setRestaurantName(name != null ? name : sub);
            newUser.setPasswordHash(""); // No password for OAuth
            newUser.setPhoneNumber("");
            newUser.setAddress("");
            newUser.setCuisineType("");
            newUser.setLogo("");
            return userRepository.save(newUser);
        });

        // Generate JWT
        String token = authService.getJwtService().generateToken(user.getEmail());

        // Redirect to frontend with token
        String frontendUrl = "https://digitalmenuuu.netlify.app/restaurant-dashboard?token=" + token;
        response.sendRedirect(frontendUrl);
    }
}
