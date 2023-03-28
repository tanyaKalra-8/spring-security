package com.example.springsecurity.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){

        UserDetails student = User.withUsername("student1")
                .password(getPasswordEncoder().encode("student"))
                .roles("STUDENT")
                .build();

        UserDetails teacher = User.withUsername("teacher1")
                .password("teacher")
                .roles("TEACHER")
                .build();

        return new InMemoryUserDetailsManager(student,teacher);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/visitor/**")
                .permitAll()
                .antMatchers("/student/**")
                .hasRole("STUDENT")
                .antMatchers("/teacher/**")
                .hasRole("TEACHER")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();

        return http.build();
    }
}
