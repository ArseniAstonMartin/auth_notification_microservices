package com.krainet.auth.config

import com.krainet.auth.service.CustomUserDetailsService
import com.krainet.auth.service.TokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration(
    private val authenticationProvider: org.springframework.security.authentication.AuthenticationProvider,
) {

    @Bean
    fun jwtAuthenticationFilter(
        userDetailsService: CustomUserDetailsService,
        tokenService: TokenService,
    ): JwtAuthenticationFilter =
        JwtAuthenticationFilter(userDetailsService, tokenService)

    @Bean
    fun jwtAuthenticationFilterRegistration(
        jwtAuthenticationFilter: JwtAuthenticationFilter,
    ): FilterRegistrationBean<JwtAuthenticationFilter> =
        FilterRegistrationBean(jwtAuthenticationFilter).apply {
            isEnabled = false
        }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
    ): DefaultSecurityFilterChain =
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        "/api/auth/login",
                        "/api/auth/register/user",
                        "/api/auth/register/admin",
                        "/api/admin-emails",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/error",
                    )
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}
