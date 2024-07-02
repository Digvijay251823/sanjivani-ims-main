package com.sanjivani.lms.controller;

import com.sanjivani.lms.interfaces.AuthService;
import com.sanjivani.lms.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthService authService;

    @GetMapping(value = "/authenticate")
    @ResponseBody
    public ResponseEntity<Collection<Role>> authenticate(@RequestParam(name = "email") String email,
                                                        @RequestParam(name = "password") String password) {
        try {
            Collection<Role> roles = authService.authenticate(email, password);
            if(null == roles) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return ResponseEntity.ok(roles);
        } catch(AccessDeniedException accessDeniedException) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = "/user/changePassword")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_VOLUNTEER', 'ROLE_ADMIN')")
    public ResponseEntity<String> changePassword(@RequestParam(name = "password") String password) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String email = auth.getName();
        LOG.info("Email: " + email + " is changing password");
        authService.changePassword(email, password);
        return ResponseEntity.ok("Password changed successfully");
    }

    @GetMapping(value = "/admin/changePassword")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> adminChangePassword(@RequestParam(name = "email") String email,
                                                      @RequestParam(name = "password") String password) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String adminEmail = auth.getName();
        LOG.info("Admin Email: " + adminEmail + " is changing password for : " + email);
        authService.changePassword(email, password);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping(value = "/register")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> register(@RequestParam(name = "email") String email,
                                           @RequestParam(name = "password") String password,
                                           @RequestParam(name = "role") String role) {
        authService.register(email, password, role);
        return ResponseEntity.ok("User registered successfully");
    }
}
