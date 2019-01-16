package com.smort.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@ComponentScan
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private SavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;
    private SimpleUrlAuthenticationFailureHandler myFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    public SecurityJavaConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint, SavedRequestAwareAuthenticationSuccessHandler mySuccessHandler) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.mySuccessHandler = mySuccessHandler;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN")
            .and()
                .withUser("xxx").password(encoder().encode("userPass")).roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
            .and()
                .headers().frameOptions().sameOrigin()
            .and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .antMatchers("/api/v1/vendors/**").permitAll()
//                .antMatchers("/api/v1/customers/**").authenticated()
//                .antMatchers("/api/v1/categories/**").hasRole("ADMIN")
//            .and()
//                .formLogin()
//                .successHandler(mySuccessHandler)
//                .failureHandler(myFailureHandler)
            .and()
                .logout();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
