package net.javaguides.springsecurity.config;

import net.javaguides.springsecurity.exception.CustomAccessDeniedHandler;
import net.javaguides.springsecurity.exception.CustomAuthenticationEntryPoint;
import net.javaguides.springsecurity.security.JwtAuthenticationEntryPoint;
import net.javaguides.springsecurity.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {

    // For Database Authentication
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter authenticationFilter;

    public SpringSecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationFilter authenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // For Database Authentication
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests((requests) -> requests
//                .requestMatchers(HttpMethod.GET, "/user", "/admin").authenticated()
//                .requestMatchers(HttpMethod.GET, "/welcome").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/user").hasAnyRole("USER", "ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/welcome").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .anyRequest().authenticated()
        );
//                .anyRequest()
//                .denyAll());
//                .permitAll());

        // Defaults
//        http.formLogin(withDefaults());
        http.formLogin(form -> form.disable());
//        http.httpBasic(withDefaults());
//        http.httpBasic(httpBasic -> httpBasic.disable());

        // For custom Basic Authentication Exception handling
//        http.httpBasic(basicAuth -> basicAuth
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        // For custom JWT Authentication Exception handling
        http.httpBasic(basicAuth -> basicAuth
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        // Tell Spring Security to use JwtAuthenticationFilter before any other filter (typically before the UsernamePasswordAuthenticationFilter)
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // For custom access denied exception handling
        http.exceptionHandling(ex -> ex
                .accessDeniedHandler(new CustomAccessDeniedHandler()));

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    // For in-memory authentication
//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder().encode("user123"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin123"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }
}
