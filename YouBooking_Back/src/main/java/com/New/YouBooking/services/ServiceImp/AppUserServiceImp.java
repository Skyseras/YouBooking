package com.New.YouBooking.services.ServiceImp;

import com.New.YouBooking.models.AppUser;
import com.New.YouBooking.models.AppUserRole;
import com.New.YouBooking.repositories.AppUserRepository;
import com.New.YouBooking.repositories.AppUserRoleRepository;
import com.New.YouBooking.services.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
//use logging
@Slf4j
public class AppUserServiceImp implements AppUserService, UserDetailsService {
    private final AppUserRepository userRepository;
    private final AppUserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(userName);
        if (user == null) {
            log.error("User not found.");
            throw new UsernameNotFoundException("User not found.");
        }else {
            log.error("User found: {}.", userName);
        }
        // set roles to user from Spring Security
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getAppUserRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        //log.error("Roles to user, authorities as: {}.", authorities);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
    @Override
    public AppUser saveUser(AppUser user) {
        log.info("Saving new user {} to database.", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUserRole userRole = userRoleRepository.findByName("ROLE_USER");
        user.getAppUserRoles().add(userRole);
        return userRepository.save(user);
    }

    @Override
    public AppUserRole saveUserRole(AppUserRole userRole) {
        log.info("Saving new user role {} to database.", userRole.getName());
        return userRoleRepository.save(userRole);
    }

    @Override
    public void addUserRoleToUser(String userName, String roleName) {
        log.info("Adding role {} to user {}.", roleName, userName);
        AppUser user = userRepository.findByUsername(userName);
        AppUserRole userRole = userRoleRepository.findByName(roleName);
        user.getAppUserRoles().add(userRole);
    }

    @Override
    public void deleteUserRoleFromUser(String userName, String roleName) {
        log.info("removing role {} from user {}.", roleName, userName);
        AppUser user = userRepository.findByUsername(userName);
        AppUserRole userRole = userRoleRepository.findByName(roleName);
        user.getAppUserRoles().remove(userRole);
    }

    @Override
    public AppUser getUser(String userName) {
        log.info("Fetching user {}.", userName);
        return userRepository.findByUsername(userName);
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Fetching all users.");
        return userRepository.findAll();
    }

    @Override
    public AppUser getUserById(Long id) {
        log.info("Fetching user {}.", id);
        Optional<AppUser> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new IllegalStateException("user not found");
        }
    }

    @Override
    public AppUser updateUser(Long id, AppUser user) {
        log.info("updating user.");
        AppUser userExist = this.getUserById(id);
        if (userExist == null) {
            throw new IllegalStateException("No user found");
        }
        userExist.setName(user.getName());
        userExist.setUsername(user.getUsername());
        userExist.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(userExist);
    }

    @Override
    public AppUser signUp(String roleName, AppUser user) {
        AppUser userByUsername = userRepository.findByUsername(user.getUsername());
        if (userByUsername != null) {
            throw new IllegalStateException("already exist");
        }
        if (roleName == null) {
            throw new IllegalStateException("needs a role");
        }
        AppUserRole role = userRoleRepository.findByName(roleName);
        if (role == null) {
            throw new IllegalStateException("role doesn't exist");
        }
        user.getAppUserRoles().add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
