package com.mjtech.mybuddy.config;


import com.mjtech.mybuddy.utility.SecurityUtility;
import com.mjtech.mybuddy.web.service.impl.CustomAccessDeniedHandlerService;
import com.mjtech.mybuddy.web.service.impl.CustomOidcUserService;
import com.mjtech.mybuddy.web.service.impl.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * SecurityConfig. Class that implement the security logic
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  /**
   * PUBLIC_MATCHERS. authorized urls.
   */
  private static final String[] PUBLIC_MATCHERS = {
          "/css/**",
          "/js/**",
          "/images/**",
          "/",
          "/loginUser",
          "/newUser",
          "/signup",
          "/login",
          "/contact",
          "/contact/add",
          "/home",
          "/access-denied"
  };
  @Autowired
  private UserSecurityService userSecurityService;
  @Autowired
  private CustomOidcUserService customOidcUserService;

  /**
   * BCryptPasswordEncoder. An instance of BCryptPasswordEncoder
   * to encrypt the password.
   *
   * @return the password encoder
   */
  @Autowired
  private BCryptPasswordEncoder passwordEncoder() {
    return SecurityUtility.passwordEncoder();
  }

  /**
   * AccessDeniedHandler. instance of accessDeniedHandler that intercepts
   * unauthorized routes.
   *
   * @return the  sendRedirect
   */
  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new CustomAccessDeniedHandlerService();
  }

  /**
   * Configure the AuthenticationManagerBuilder to use the password encoder.
   *
   * @param auth AuthenticationManagerBuilder instance
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
  }

  /**
   * Configure web based security for specific http requests.
   *
   * @param http the HttpSecurity
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
            .authorizeRequests()
            .antMatchers("/h2/**").permitAll()
            .antMatchers(PUBLIC_MATCHERS).permitAll()
            .antMatchers("/myProfile/admin/**").hasAnyRole("ADMIN")
            .antMatchers("/myProfile/user/**").hasAnyRole("USER", "ADMIN")
            .anyRequest().authenticated();

    http
            .csrf().disable().cors().disable()
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
            .and()
            .formLogin()
            .loginPage("/login").permitAll()
            .defaultSuccessUrl("/myProfile", true)
            .and()
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler())
            .and()
            .rememberMe()
            .and()
            .oauth2Login()
            .loginPage("/login")
            .userInfoEndpoint()
            .oidcUserService(customOidcUserService);
  }



}
