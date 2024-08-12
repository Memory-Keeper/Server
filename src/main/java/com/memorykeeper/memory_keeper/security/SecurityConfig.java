package com.memorykeeper.memory_keeper.security;

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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // 인증을 비활성화 할 엔드포인트들
                        .requestMatchers(
                                "/api/users/send-verification-code",
                                "/api/users/verify-code",
                                "/api/users/register",
                                "/api/users/reset-password-request",
                                "/api/users/reset-password",
                                "/api/users/find-username-request",
                                "/api/users/find-username",
                                "/api/dementia-test-results/save", // 치매 자가진단 테스트 결과 저장 엔드포인트
                                "/api/dementia-test-results/user/**", // 사용자별 자가진단 테스트 조회
                                "/api/cognitive-training/submit", // 퀴즈 결과 저장 엔드포인트
                                "/api/cognitive-training/user/**", // 사용자별 퀴즈 결과 조회 엔드포인트 추가
                                "/api/dementia-centers/fetch-and-save", // 치매센터 데이터 수집 엔드포인트
                                "/api/users/{userId}/dementia-centers" // 사용자별 치매센터 리스트 조회
                        ).permitAll() // 인증 없이 접근 허용
                        .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
                )
                .formLogin(login -> login // 폼 로그인 설정
                        //.loginPage("/login") // 사용자 정의 로그인 페이지 설정 (구현 시 주석 해제)
                        .defaultSuccessUrl("/home", true) // 로그인 성공 시 리디렉션할 URL 설정
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





