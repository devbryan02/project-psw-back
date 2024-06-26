package com.project.projectaquiler.config.security;

import com.project.projectaquiler.config.security.filter.JwtTokenValidator;
import com.project.projectaquiler.services.auth.UserDetailsImpl;
import com.project.projectaquiler.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

  private JwtUtils jwtUtils;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(AbstractHttpConfigurer::disable)
      .httpBasic(Customizer.withDefaults())
      .sessionManagement(session -> {
        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        session.maximumSessions(1);
      })
      .authorizeHttpRequests(httpRequest -> {
        // endpoinst publicos
        httpRequest.requestMatchers(HttpMethod.POST, "/auth/**").permitAll();
        httpRequest
          .requestMatchers(HttpMethod.GET, "/vehicle/list")
          .permitAll();
        httpRequest.requestMatchers(HttpMethod.POST, "/boletin/create").permitAll();
        httpRequest
          .requestMatchers(HttpMethod.GET, "vehicle/search/{palabra}")
          .permitAll();
        httpRequest
          .requestMatchers(HttpMethod.GET, "vehicle/filter/max")
          .permitAll();
        httpRequest
                .requestMatchers(HttpMethod.GET, "vehicle/filter/min")
                .permitAll();
        httpRequest
          .requestMatchers(HttpMethod.POST, "/user/create")
          .permitAll();
        httpRequest.requestMatchers(HttpMethod.GET, "/vehicle/{id}").permitAll(); 

        //endpoinst configurados
        httpRequest.requestMatchers(HttpMethod.GET, "/boletin/list").hasRole("ADMIN");
        httpRequest
          .requestMatchers(HttpMethod.POST, "/vehicle/create")
          .hasRole("ADMIN");
        httpRequest.requestMatchers(HttpMethod.PATCH, "/vehicle/update/{vehicleId}")
          .hasRole("ADMIN");
        httpRequest.requestMatchers(HttpMethod.DELETE, "/vehicle/delete/{id}").hasRole("ADMIN");
        httpRequest
          .requestMatchers(HttpMethod.GET, "/user/list")
          .hasRole("ADMIN");
        httpRequest.requestMatchers(HttpMethod.POST, "/booking/create").hasRole("ADMIN");
        httpRequest.requestMatchers(HttpMethod.GET, "/booking/list").hasRole("ADMIN");
        httpRequest.requestMatchers(HttpMethod.GET, "/booking/{idBooking}").hasRole("ADMIN");

        // endpoints no configuradas
        httpRequest.anyRequest().denyAll();
      })
      .addFilterBefore(
        new JwtTokenValidator(jwtUtils),
        BasicAuthenticationFilter.class
      )
      .build();
  }

  @Bean
  AuthenticationProvider provider(UserDetailsImpl userDetailsService) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(userDetailsService);
    return provider;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
