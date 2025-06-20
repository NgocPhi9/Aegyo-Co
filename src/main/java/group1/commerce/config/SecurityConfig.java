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
                    auth.requestMatchers("/css/**", "/js/**", "/images/**").permitAll();
                    auth.requestMatchers("/4Moos", "/4Moos/index", "/4Moos/login", "/4Moos/register").permitAll();
                    auth.requestMatchers("/4Moos/product/**").permitAll();

                    // IMPORTANT: Admin routes must be defined BEFORE the general /4Moos/** rule
                    auth.requestMatchers("/4Moos/admin/**").hasAuthority("ROLE_ADMIN");

                    // Protected routes requiring authentication
                    auth.requestMatchers("/4Moos/profile,**", "/4Moos/orders,**", "/4Moos/order/**").authenticated();

                    auth.requestMatchers("/4Moos/**").permitAll();

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
