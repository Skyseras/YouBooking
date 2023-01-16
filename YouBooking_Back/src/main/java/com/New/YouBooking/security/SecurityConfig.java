package com.New.YouBooking.security;

import com.New.YouBooking.jwtfilters.CustomAuthenticationFilter;
import com.New.YouBooking.jwtfilters.CustomAuthorisationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    //configure security with JWT (JSON Web Token) system, token system
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Login and Token Refresh
        // set user access to routes based on user roles
        // permit access to all for path /api/login/** ("**" is anything that came after that "/api/login/"), etc.
        http.authorizeRequests().antMatchers("/api/login/**", "/api/token/refresh/**").permitAll();
        http.authorizeRequests().antMatchers("/api/signUpUser").permitAll();

        // User
        // permit read, GET on /api/user/** for user with role ROLE_USER
        // you can use also hasAnyRole() and give array of roles
        http.authorizeRequests().antMatchers(GET, "/api/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/api/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(PUT, "/api/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(DELETE, "/api/**").hasAnyAuthority("ROLE_USER");

        /*
        http.authorizeRequests().antMatchers(GET, "/api/user/**").hasAnyAuthority("ROLE_USER");
        // permit insert new data, POST on /api/user/save/** for user with role ROLE_ADMIN
        http.authorizeRequests().antMatchers(POST, "/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/user/update/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/user/delete/**").hasAnyAuthority("ROLE_ADMIN");

        // Hotel
        http.authorizeRequests().antMatchers(GET, "/api/hotel/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/api/hotel/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/hotel/update/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/hotel/delete/**").hasAnyAuthority("ROLE_ADMIN");

        // Client
        http.authorizeRequests().antMatchers(GET, "/api/client/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/api/client/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/client/update/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/client/delete/**").hasAnyAuthority("ROLE_ADMIN");

        // Room
        http.authorizeRequests().antMatchers(GET, "/api/room/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/api/room/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/room/update/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/room/delete/**").hasAnyAuthority("ROLE_ADMIN");

        // Reservation
        http.authorizeRequests().antMatchers(GET, "/api/reservation/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(POST, "/api/reservation/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/reservation/update/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/reservation/delete/**").hasAnyAuthority("ROLE_ADMIN");
        */
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilter(corsFilter());
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200/"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin","Access-Control-Allow-Origin",
                "Content-Type","Accept","Authorization","Origin,Accept","X-Requested-With",
                "Access-Control-Request-Method","Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin","Content-Type","Accept","Authorization",
                "Access-Control-Allow-Origin","Access-Control-Allow-Origin","Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET","PUT","POST","DELETE","OPTIONS"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);

    }
}
