package com.krainet.auth.config

import com.krainet.auth.service.CustomUserDetailsService
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableCaching
@EnableConfigurationProperties(
    JwtProperties::class,
    KafkaTopicProperties::class,
    InternalApiProperties::class,
)
class AppConfiguration {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(
        userDetailsService: CustomUserDetailsService,
        passwordEncoder: PasswordEncoder,
    ): AuthenticationProvider =
        DaoAuthenticationProvider(userDetailsService).also {
            it.setPasswordEncoder(passwordEncoder)
        }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager

    @Bean
    fun userTopic(kafkaTopicProperties: KafkaTopicProperties): NewTopic =
        TopicBuilder
            .name(kafkaTopicProperties.userTopic)
            .partitions(1)
            .replicas(1)
            .build()
}
