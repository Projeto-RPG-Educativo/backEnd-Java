package com.game.rpgbackend.controller;

import com.game.rpgbackend.dto.request.UpdateProfileRequest;
import com.game.rpgbackend.dto.response.UserProfileResponse;
import com.game.rpgbackend.dto.response.UserStatsResponse;
import com.game.rpgbackend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        UserProfileResponse profile = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateProfileRequest request) {
        UserProfileResponse updated = userService.updateProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/stats")
    public ResponseEntity<UserStatsResponse> getStats(@AuthenticationPrincipal UserDetails userDetails) {
        UserStatsResponse stats = userService.getUserStats(userDetails.getUsername());
        return ResponseEntity.ok(stats);
    }
}
