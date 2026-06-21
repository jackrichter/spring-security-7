package net.javaguides.springsecurity.config;

import net.javaguides.springsecurity.exception.CustomAuthenticationEntryPoint;
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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class SpringSecurityConfig {

    // For Database Authentication
    private UserDetailsService userDetailsService;

    public SpringSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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
                        .anyRequest().authenticated()
        );
//                .anyRequest()
//                .denyAll());
//                .permitAll());

        // Defaults
//        http.formLogin(withDefaults());
        http.formLogin(formLogin -> formLogin.disable());
//        http.httpBasic(withDefaults());
//        http.httpBasic(httpBasic -> httpBasic.disable());

        // For custom authentication exception handling
        http.httpBasic(basicAuth -> basicAuth
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()));


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
