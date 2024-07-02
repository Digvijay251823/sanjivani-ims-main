package com.sanjivani.lms.service;

import com.sanjivani.lms.entity.User;
import com.sanjivani.lms.entity.UserRoleEntity;
import com.sanjivani.lms.entity.UserRoleKey;
import com.sanjivani.lms.interfaces.AuthService;
import com.sanjivani.lms.model.Role;
import com.sanjivani.lms.repository.RoleRepository;
import com.sanjivani.lms.repository.UserRepository;
import com.sanjivani.lms.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.FailedLoginException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private SanjivaniUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public Collection<Role> authenticate(@NonNull String email, @NonNull String password) throws AccessDeniedException {
        //Check password match
        // Get the User from UserDetailsService
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String correctEncodedPassword = userDetails.getPassword();

        // Authenticate
        // If Passwords don't match throw an exception
        String encodedPassword = passwordEncoder.encode(password);
        if(!passwordEncoder.matches(password, correctEncodedPassword))
            throw new AccessDeniedException("Incorrect Credentials, access denied");

        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication authentication =  new UsernamePasswordAuthenticationToken(email, correctEncodedPassword, grantedAuths);
        boolean isAuthenticated = authentication.isAuthenticated();
        if(!isAuthenticated) {
            LOG.error("Token creation failed for email: " + email);
            return null;
        }
        LOG.info("Token creation successful for email: " + email);
        Collection<Role> roles = new ArrayList<>();
        User user = userRepository.findByEmail(email);

        for(UserRoleEntity userRoleEntity : user.getUserRoles()) {
            com.sanjivani.lms.entity.Role roleEntity = userRoleEntity.getRole();
            if(null == roleEntity) {
                LOG.error("Role not found for user: " + email);
                return null;
            }
            Long id = roleEntity.getId();
            if(id <= 0L) {
                LOG.error("Role id is invalid for user: " + email);
                return null;
            }
            String name = roleEntity.getName();
            if(null == name || name.isEmpty()) {
                LOG.error("Role name is invalid for user: " + email);
                return null;
            }
            Role role = Role.builder()
                    .id(id)
                    .name(name)
                    .created(roleEntity.getCreated())
                    .modified(roleEntity.getModified())
                    .build();
            roles.add(role);
        }
        return roles;
    }

    @Override
    @Transactional
    public void changePassword(@NonNull String email, @NonNull String password) throws IllegalArgumentException {
        if(email.isEmpty() || password.isEmpty()) {
            LOG.error("Email or password is null or empty. Cannot change password.");
            return;
        }
        User user = userRepository.findByEmail(email);
        if (null == user) {
            LOG.error("User not found for email: " + email);
            throw new IllegalArgumentException("User not found for email: " + email);
        }
        LOG.info("Changing password for email: " + email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void register(@NonNull String email, @NonNull String password, @NonNull String role) throws IllegalArgumentException {
        if(email.isEmpty() || password.isEmpty() || role.isEmpty())
            throw new IllegalArgumentException("Email, password or role is null or empty");
        com.sanjivani.lms.entity.Role roleEntity = roleRepository.findByName(role);
        if(roleEntity == null)
            throw new IllegalArgumentException(role + "Given role not found. Cannot register user.");

        User user = userRepository.findByEmail(email);
        if(null == user) {
            user = userRepository.save(User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .enabled(true)
                    .build());
        }

        Long userId = user.getId();
        if(userId <= 0L)
            throw new IllegalArgumentException("User id is invalid");

        Long roleId = roleEntity.getId();
        if(roleId <= 0L)
            throw new IllegalArgumentException("Role id is invalid");


        UserRoleKey userRoleKey = UserRoleKey.builder()
                .userId(userId)
                .roleId(roleId)
                .build();

        //Give the requested Role to the user
        UserRoleEntity userRoleEntity = userRoleRepository.findById(userRoleKey).orElse(null);
        if(null == userRoleEntity) {
            userRoleRepository.save(UserRoleEntity.builder()
                    .id(userRoleKey)
                    .user(user)
                    .role(roleEntity)
                    .build());
        }

        com.sanjivani.lms.entity.Role roleUserEntity = roleRepository.findByName("ROLE_USER");
        if(null == roleUserEntity)
            throw new IllegalArgumentException("ROLE_USER role not found");
        Long roleUserId = roleUserEntity.getId();
        if(roleUserId <= 0L)
            throw new IllegalArgumentException("ROLE_USER role id is invalid");
        UserRoleKey userRoleUserKey = UserRoleKey.builder()
                .userId(userId)
                .roleId(roleUserId)
                .build();

        //Give the ROLE_USER Role to the user by default
        UserRoleEntity userRoleUserEntity = userRoleRepository.findById(userRoleUserKey).orElse(null);
        if(null == userRoleUserEntity) {
            userRoleRepository.save(UserRoleEntity.builder()
                    .id(userRoleUserKey)
                    .user(user)
                    .role(roleUserEntity)
                    .build());
        }
    }
}
