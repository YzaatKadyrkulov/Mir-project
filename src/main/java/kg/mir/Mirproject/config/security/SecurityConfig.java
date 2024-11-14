package kg.mir.Mirproject.config.security;

import kg.mir.Mirproject.config.security.jwt.JwtFilter;
import kg.mir.Mirproject.exception.NotFoundException;
import kg.mir.Mirproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final UserRepository userRepository;
    private final JwtFilter jwtFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.getUserByEmail(email)
                .orElseThrow(() -> new NotFoundException("Not found user with email: " + email));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    // Разрешаем доступ с указанных доменов
                    corsConfiguration.addAllowedOrigin("http://localhost:5173");
                    corsConfiguration.addAllowedOrigin("http://mir-nori.ru");
                    corsConfiguration.addAllowedOrigin("https://ec2-3-127-65-10.eu-central-1.compute.amazonaws.com");
                    corsConfiguration.addAllowedOrigin("http://ec2-3-127-65-10.eu-central-1.compute.amazonaws.com:8080");
                    corsConfiguration.addAllowedOrigin("http://ec2-3-127-65-10.eu-central-1.compute.amazonaws.com");
                    corsConfiguration.addAllowedMethod("*");
                    corsConfiguration.addAllowedHeader("*");
                    return corsConfiguration;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/auth/**", "/api/awsS3/**", "/swagger-ui/**", "/v3/api-docs/**")
                        .permitAll()
                        .anyRequest()
                        .permitAll())  // Здесь вы разрешаете все запросы без аутентификации, если необходимо
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
}
