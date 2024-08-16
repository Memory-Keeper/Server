package com.memorykeeper.memory_keeper.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
                .headers(headers -> headers
                        .addHeaderWriter(new StaticHeadersWriter("X-Content-Type-Options", "nosniff")) // 헤더 추가
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/users/send-verification-code",
                                "/api/users/verify-code",
                                "/api/users/register",
                                "/api/users/reset-password-request",
                                "/api/users/reset-password",
                                "/api/users/find-username-request",
                                "/api/users/find-username"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class) // JWT 필터 추가
                .formLogin(login -> login
                        .successHandler(authenticationSuccessHandler()) // 로그인 성공 시 JSON 응답 반환
                        .failureHandler(authenticationFailureHandler()) // 로그인 실패 시 JSON 응답 반환
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(userDetailsService, jwtTokenUtil);
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // 로그인 성공 시 JWT 토큰 생성
            String token = jwtTokenUtil.generateToken(authentication.getName());

            // CustomUserDetails로 캐스팅
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            Long userId = userDetails.getId();

            // 사용자 ID와 Username을 반환 데이터에 포함
            Map<String, Object> data = new HashMap<>();
            data.put("statusCode", HttpServletResponse.SC_OK);
            data.put("message", "Login successful");
            data.put("token", token);  // JWT 토큰 응답에 포함
            data.put("username", username); // username 추가
            data.put("userId", userId); // userId 추가
            data.put("timestamp", LocalDateTime.now().toString());

            response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(data));
        };
    }


    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Map<String, Object> data = new HashMap<>();
            data.put("statusCode", HttpServletResponse.SC_UNAUTHORIZED);
            data.put("message", "Login failed: " + exception.getMessage());
            data.put("timestamp", LocalDateTime.now().toString());

            response.getWriter().write(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(data));
        };
    }

    // CORS 설정을 위한 CorsConfigurationSource 빈 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // 모든 도메인 허용
        configuration.addAllowedHeader("*"); // 모든 헤더 허용
        configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
        configuration.setAllowCredentials(true); // 인증 정보 포함 허용
        configuration.addExposedHeader("Access-Control-Allow-Origin"); // 클라이언트에서 확인 가능하도록 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(); // CustomUserDetailsService 클래스 사용
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}







