package bsb.group5.converter.config;


import bsb.group5.converter.controllers.filters.AuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationFilter authenticationFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and().csrf().disable();
        httpSecurity.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
