package com.educator.profile;

import com.educator.profile.dto.ChangePasswordRequest;
import com.educator.profile.dto.ProfileResponse;
import com.educator.profile.dto.UpdateProfileRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/learner/profile")
public class LearnerProfileController {

    private final ProfileService profileService;

    public LearnerProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(Authentication authentication) {
        return ResponseEntity.ok(profileService.getProfile(resolveEmail(authentication)));
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(profileService.updateProfile(resolveEmail(authentication), request.getEmail()));
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        profileService.changePassword(resolveEmail(authentication), request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    private String resolveEmail(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new IllegalArgumentException("Authenticated user email is required");
        }
        return authentication.getName();
    }
}
