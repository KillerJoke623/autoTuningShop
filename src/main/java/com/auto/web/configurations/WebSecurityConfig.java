package com.auto.web.configurations;

import com.auto.web.services.OAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig{
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Autowired
    private OAuth2UserService oAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        UserDetailsService userDetailsService = userDetailsServiceImpl;
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/admin/**")).hasRole("ADMIN")

                        .requestMatchers(AntPathRequestMatcher.antMatcher("/login"),
                                AntPathRequestMatcher.antMatcher("/registration"),
                                AntPathRequestMatcher.antMatcher("/"),
                                AntPathRequestMatcher.antMatcher("/servicesPage"),
                                AntPathRequestMatcher.antMatcher("/css/**"),  // Разрешаем доступ к CSS
                                AntPathRequestMatcher.antMatcher("/images/**"), // Разрешаем доступ к images
                                AntPathRequestMatcher.antMatcher("/js/**"), // Разрешаем доступ к JavaScript
                                AntPathRequestMatcher.antMatcher("/oauth2/**"))
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login ->  // Настройка oauth2Login в отдельном блоке lambda
                        oauth2Login
                                .loginPage("/login")
                                .userInfoEndpoint()
                                .oidcUserService(oAuth2UserService)
                )
                .formLogin(
                        form -> form
                                .loginPage("/login")
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }

    //!TODO Убрать костыль в services
    //!TODO написать нормал конфиг, который работает, потому что сейчас на дефолте всё ок, но при этом не используется login.html и неизвестно, используется ли LoginController
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .requestMatchers("/", "/registration", "/css/**", "/js/**", "/login", "/registration-success", "/registration/registration-success", "/register", "/services")
//                .permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        if (request.getUserPrincipal() != null) {
//                            response.sendRedirect("/services");
//                        } else {
//                            response.sendRedirect("/login");
//                        }
//                    }
//                })
//                .failureHandler(new AuthenticationFailureHandler() {
//                    @Override
//                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//                        request.setAttribute("errorMessage", exception.getMessage());
//                        request.getRequestDispatcher("/login").forward(request, response);
//                    }
//                })
//                .and()
//                .csrf()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
//        return http.build();
//    }
}

