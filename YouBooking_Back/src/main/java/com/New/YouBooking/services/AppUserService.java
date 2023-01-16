package com.New.YouBooking.services;

import com.New.YouBooking.models.AppUser;
import com.New.YouBooking.models.AppUserRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface AppUserService {
    AppUser saveUser (AppUser user);
    AppUserRole saveUserRole (AppUserRole userRole);
    void addUserRoleToUser(String username, String userRoleName);
    void deleteUserRoleFromUser(String username, String userRoleName);
    AppUser getUser(String Username);
    AppUser getUserById(Long id);
    List<AppUser> getUsers();
    AppUser updateUser(Long id,AppUser user);
    AppUser signUp(String roleName, AppUser user);
}
