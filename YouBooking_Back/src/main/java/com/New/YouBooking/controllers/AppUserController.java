package com.New.YouBooking.controllers;

import com.New.YouBooking.models.AppUser;
import com.New.YouBooking.models.AppUserRole;
import com.New.YouBooking.services.AppUserService;
import com.New.YouBooking.services.Payload.UserRoleToUserForm;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class AppUserController {
    private final AppUserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }
    @PostMapping("/user/save")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUser user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }
    @PostMapping("/role/save")
    public ResponseEntity<AppUserRole> saveRole(@RequestBody AppUserRole userRole) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUserRole(userRole));
    }
    @PostMapping("/role/addroletouser")
    public ResponseEntity<?> addUserRoleToUser(@RequestBody UserRoleToUserForm form) {
        userService.addUserRoleToUser(form.getUsername(), form.getUserRoleName());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/role/deleterolefromuser")
    public ResponseEntity<?> deleteUserRoleFromUser(@RequestBody UserRoleToUserForm form) {
        userService.deleteUserRoleFromUser(form.getUsername(), form.getUserRoleName());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/signUpUser")
    public ResponseEntity<?> signUpUser(@RequestBody AppUser user){
        try {
            AppUser ROLEUSER = userService.signUp("ROLE_USER",user);
            if(ROLEUSER != null){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(ROLEUSER);
            }else {
                return ResponseEntity.badRequest().body("Not valid");
            }
        }catch (IllegalStateException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    @PostMapping("/signUpManager")
    public ResponseEntity<?> signUpManager(@RequestBody AppUser user){
        try {
            AppUser ROLEMANAGER = userService.signUp("ROLE_MANAGER",user);
            if(ROLEMANAGER != null){
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(ROLEMANAGER);
            }else {
                return ResponseEntity.badRequest().body("Not valid");
            }
        }catch (IllegalStateException e){
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorisationHeader = request.getHeader(AUTHORIZATION);
        if (authorisationHeader != null && authorisationHeader.startsWith("Bearer ")) {
            try {

                String refreshToken = authorisationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                AppUser user = userService.getUser(username);
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getAppUserRoles().stream().map(AppUserRole::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception exception) {
                log.error("Error logging in : {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
        else {
            throw new RuntimeException("Refresh token missing");
        }
    }
}
