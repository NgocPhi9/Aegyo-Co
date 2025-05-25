package group1.commerce.config;

import group1.commerce.handler.CustomLoginSuccessHandler;
import group1.commerce.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, CustomUserDetailsService customUserDetailsService) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        return builder.build();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CustomLoginSuccessHandler customLoginSuccessHandler) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/4Moos/**").permitAll();
                    auth.requestMatchers("/4Moos/register", "/4Moos/login").permitAll();
                    auth.requestMatchers("/css/**", "/js/**", "/images/**").permitAll();
                    auth.requestMatchers("/registration/**", "/login/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/4Moos/login")
                        .successHandler(customLoginSuccessHandler)
                )
                .formLogin(form -> form
                        .loginPage("/4Moos/login")
                        .loginProcessingUrl("/4Moos/login")
                        .successHandler(customLoginSuccessHandler)
                        .failureUrl("/4Moos/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/4Moos/logout") // Define your logout URL
                        .logoutSuccessUrl("/4Moos/login")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )

                .build();
    }

}
