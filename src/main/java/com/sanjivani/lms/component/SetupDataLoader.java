package com.sanjivani.lms.component;

import com.sanjivani.lms.entity.*;
import com.sanjivani.lms.repository.*;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private RolePrivilegeRepository rolePrivilegeRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        Role volunteerRole = createRoleIfNotFound("ROLE_VOLUNTEER");
        Role userRole = createRoleIfNotFound("ROLE_USER");

        createRolePrivilegeIfNotFound(adminRole, readPrivilege);
        createRolePrivilegeIfNotFound(adminRole, writePrivilege);
        createRolePrivilegeIfNotFound(volunteerRole, readPrivilege);
        createRolePrivilegeIfNotFound(volunteerRole, writePrivilege);
        createRolePrivilegeIfNotFound(userRole, readPrivilege);

        User adminUser = createUserIfNotFound("phani.karthik@gmail.com","Prabhupada@108");
        User volUser = createUserIfNotFound("parthprandas.rns@gmail.com","Prabhupada@108");

        createUserRoleIfNotFound(adminUser, adminRole);
        createUserRoleIfNotFound(adminUser, volunteerRole);
        createUserRoleIfNotFound(adminUser, userRole);
        createUserRoleIfNotFound(volUser, volunteerRole);
        createUserRoleIfNotFound(volUser, userRole);

        alreadySetup = true;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = privilegeRepository.save(Privilege.builder()
                    .name(name)
                    .build());
        }
        return privilege;
    }

    @Transactional
    private Role createRoleIfNotFound(
            String name) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = roleRepository.save(Role.builder()
                    .name(name)
                    .build());
        }
        return role;
    }

    @Transactional
    private User createUserIfNotFound(
            String email, String password) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = userRepository.save(User.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .enabled(true)
                    .build());
        }
        return user;
    }

    @Transactional
    private UserRoleEntity createUserRoleIfNotFound(
            User user, Role role) {
        if(null == user || null == role) {
            return null;
        }
        UserRoleKey userRoleKey = UserRoleKey.builder()
                .userId(user.getId())
                .roleId(role.getId())
                .build();

        UserRoleEntity userRoleEntity = userRoleRepository.findById(userRoleKey).orElse(null);
        if (userRoleEntity == null) {
            userRoleEntity = userRoleRepository.save(UserRoleEntity.builder()
                    .id(userRoleKey)
                    .user(user)
                    .role(role)
                    .build());
        }
        return userRoleEntity;
    }

    @Transactional
    private RolePrivilegeEntity createRolePrivilegeIfNotFound(
            Role role, Privilege privilege) {
        if(null == role || null == privilege) {
            return null;
        }
        RolePrivilegeKey rolePrivilegeKey = RolePrivilegeKey.builder()
                .roleId(role.getId())
                .privilegeId(privilege.getId())
                .build();

        RolePrivilegeEntity rolePrivilegeEntity = rolePrivilegeRepository.findById(rolePrivilegeKey).orElse(null);
        if (rolePrivilegeEntity == null) {
            rolePrivilegeEntity = rolePrivilegeRepository.save(RolePrivilegeEntity.builder()
                    .id(rolePrivilegeKey)
                    .role(role)
                    .privilege(privilege)
                    .build());
        }
        return rolePrivilegeEntity;
    }
}
