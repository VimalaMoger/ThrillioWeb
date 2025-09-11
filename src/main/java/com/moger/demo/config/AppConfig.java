package com.moger.demo.config;

import com.moger.demo.customValidators.BookValidator;
import com.moger.demo.customValidators.UserValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Value("${user.password}")
    public String userPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserValidator userValidator() {
        return new UserValidator();
    }

    @Bean
    public BookValidator bookValidator() {
        return new BookValidator();
    }

}





