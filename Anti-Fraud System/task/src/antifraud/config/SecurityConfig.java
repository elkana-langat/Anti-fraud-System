package antifraud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable) // For modifying
                // requests via postman
                .headers(headers -> headers.frameOptions().disable()) // for
                // Postman, the H2 console
                .authorizeHttpRequests(auth -> auth // manage access
                        .requestMatchers(HttpMethod.POST, "/api/antifraud" +
                                "/transaction").permitAll()
                        .requestMatchers("/actuator/shutdown").permitAll() //
                        // needs to run test
                        .requestMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        // no session
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
