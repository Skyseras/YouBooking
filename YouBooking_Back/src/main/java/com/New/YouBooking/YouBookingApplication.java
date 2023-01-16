package com.New.YouBooking;

import com.New.YouBooking.models.AppUser;
import com.New.YouBooking.models.AppUserRole;
import com.New.YouBooking.services.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class YouBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(YouBookingApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}


/*
	// save data in DB for users, roles, roles to users
	@Bean
	CommandLineRunner run (AppUserService userService) {
		return args -> {
			userService.saveUserRole(new AppUserRole(null,"ROLE_USER"));
			userService.saveUserRole(new AppUserRole(null,"ROLE_MANAGER"));
			userService.saveUserRole(new AppUserRole(null,"ROLE_ADMIN"));
			userService.saveUserRole(new AppUserRole(null,"ROLE_SUPER_ADMIN"));

			userService.saveUser(new AppUser(null,"Admin Admin","admin","123456", new ArrayList<>(),new ArrayList<>()));
			userService.saveUser(new AppUser(null,"John Daniel","john","123456", new ArrayList<>(),new ArrayList<>()));

			userService.addUserRoleToUser("admin","ROLE_USER");
			userService.addUserRoleToUser("admin","ROLE_MANAGER");
			userService.addUserRoleToUser("admin","ROLE_ADMIN");
			userService.addUserRoleToUser("john","ROLE_MANAGER");
			userService.addUserRoleToUser("john","ROLE_SUPER_ADMIN");
		};
	}

 */
}
