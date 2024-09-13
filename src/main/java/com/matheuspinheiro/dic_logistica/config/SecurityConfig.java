package com.matheuspinheiro.dic_logistica.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.matheuspinheiro.dic_logistica.security.JWTAuthenticationFilter;
import com.matheuspinheiro.dic_logistica.security.JWTAuthorizationFilter;
import com.matheuspinheiro.dic_logistica.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

        private AuthenticationManager authenticationManager;

        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        private JWTUtil jwtUtil;

        private static final String[] PUBLIC_MATCHERS = {
                        "/**"
        };

        private static final String[] PUBLIC_MATCHER_POST = {
                        "/user/**",
                        "/login/**",
                        "/dicionario/**",
                        "/dicionario/palavras",
                        "/dicionario/palavras/**"
        };

        private static final String[] PUBLIC_MATCHER_GET = {
                        "/user/**",
                        "/dicionario/**",
                        "/dicionario/palavras",
                        "/dicionario/palavras/**"

        };

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.cors(AbstractHttpConfigurer::disable)
                                .csrf(AbstractHttpConfigurer::disable);

                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(this.userDetailsService)
                                .passwordEncoder(bCryptPasswordEncoder());
                this.authenticationManager = authenticationManagerBuilder.build();

                http
                                .authorizeHttpRequests(authz -> authz
                                                .requestMatchers(HttpMethod.POST, PUBLIC_MATCHER_POST).permitAll()
                                                .requestMatchers(PUBLIC_MATCHERS).permitAll()
                                                .requestMatchers(HttpMethod.GET, PUBLIC_MATCHER_GET)
                                                .hasAnyRole("USER", "ADMIN")
                                                .anyRequest().authenticated())
                                .authenticationManager(authenticationManager)
                                .addFilter(new JWTAuthenticationFilter(this.authenticationManager, this.jwtUtil));
                http
                                .addFilter(new JWTAuthorizationFilter(this.authenticationManager, this.jwtUtil,
                                                this.userDetailsService));
                http
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.build();
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
                configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
                final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }

        @Bean
        public BCryptPasswordEncoder bCryptPasswordEncoder() {
                return new BCryptPasswordEncoder();
        }

}
