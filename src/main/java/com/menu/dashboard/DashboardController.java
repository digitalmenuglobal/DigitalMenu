package com.menu.dashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    @GetMapping("/")
    public ResponseEntity<String> getDashboard(Authentication authentication) {
    String restaurantName = authentication.getName();
    return ResponseEntity.ok("Hello, " + restaurantName + "! Your JWT is valid.");
    }
}



