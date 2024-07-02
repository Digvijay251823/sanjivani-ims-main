package com.sanjivani.lms.interfaces;

import com.sanjivani.lms.model.Role;
import lombok.NonNull;
import org.springframework.security.access.AccessDeniedException;

import java.util.Collection;

public interface AuthService {
    Collection<Role> authenticate(@NonNull String email, @NonNull String password) throws AccessDeniedException;
    void changePassword(@NonNull String email, @NonNull String password);

    void register(@NonNull String email, @NonNull String password, @NonNull String role) throws IllegalArgumentException;
}
