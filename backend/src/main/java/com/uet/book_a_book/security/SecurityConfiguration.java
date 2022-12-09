package com.uet.book_a_book.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.uet.book_a_book.security.filter.JwtFilter;
import com.uet.book_a_book.security.filter.StaticContentFilter;
import com.uet.book_a_book.security.provider.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {
	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	@Autowired
	private JwtFilter jwtFilter;
	@Autowired
	private JwtAuthenEntryPoint jwtAuthenEntryPoint;
	@Autowired
	private CustomAccessDeniedHandler customAccessDeniedHandler;
	@Autowired
	private StaticContentFilter staticContentFilter;

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.authenticationProvider(customAuthenticationProvider)
				.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors()
				.and()
				.csrf().disable()
				.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenEntryPoint)
				.accessDeniedHandler(customAccessDeniedHandler)
				.and()
				.authorizeHttpRequests(
						requests -> {
							requests.antMatchers("/api/authen/**").permitAll()
									.antMatchers("/api/users/forgot_password/**").permitAll()
									.antMatchers("/api/books/**").permitAll()
									.antMatchers(HttpMethod.GET, "/api/comments").permitAll()
									.antMatchers("/swagger/**").permitAll()
									.anyRequest().authenticated();
						})
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(staticContentFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/swagger-ui/**", "/v3/api-docs/**");
	}

}
