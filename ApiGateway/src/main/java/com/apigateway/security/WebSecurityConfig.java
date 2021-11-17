package com.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public  WebSecurityConfig {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private SecurityContextRepository securityContextRepository;

	@Autowired
	private JwtWebFilter jwtWebFilter;

	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
		return http.exceptionHandling().authenticationEntryPoint((swe, e) -> {
			return Mono.fromRunnable(() -> {
				swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			});
		}).accessDeniedHandler((swe, e) -> {
			return Mono.fromRunnable(() -> {
				swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
			});
		}).and().csrf().disable().authenticationManager(authenticationManager)
				.securityContextRepository(securityContextRepository).authorizeExchange().pathMatchers("/users/login")
				.permitAll().anyExchange().authenticated().and()
				.addFilterAfter(jwtWebFilter, SecurityWebFiltersOrder.FIRST).build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
