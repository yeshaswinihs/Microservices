package com.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public  WebSecurity extends WebSecurityConfigurerAdapter {

	private Environment environment;

	@Autowired
	public WebSecurity(Environment environment) {
		this.environment = environment;
	}

//	@Value("${security.enable-csrf}")
//	private boolean csrfEnabled;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		if (!csrfEnabled) {
//			http.csrf().disable();
//		}
//		http.csrf((csrf) -> csrf.disable());
//		http.anonymous().disable();
//		http.headers().frameOptions().disable();

		// Build the request matcher for CSFR protection
//		RequestMatcher csrfRequestMatcher = new RequestMatcher() {
//
//			// Disable CSFR protection on the following urls:
//			private AntPathRequestMatcher[] requestMatchers = { new AntPathRequestMatcher("/users/**")
//
//			};
//
//			@Override
//			public boolean matches(HttpServletRequest request) {
//				// If the request match one url the CSFR protection will be disabled
//				for (AntPathRequestMatcher rm : requestMatchers) {
//					if (rm.matches(request)) {
//						return false;
//					}
//				}
//				return true;
//			}
//
//		};
//
//		http.csrf().disable().requestMatcher(csrfRequestMatcher);
//
////		http
////				// Disable the csrf protection on some request matches
////				.csrf().requireCsrfProtectionMatcher(csrfRequestMatcher);

		http.csrf().disable();
		http.headers().frameOptions().disable();
		http.authorizeRequests().antMatchers(environment.getProperty("api.h2console.url.path")).permitAll()
				.antMatchers(HttpMethod.POST, environment.getProperty("api.registration.url.path")).permitAll()
				.antMatchers(HttpMethod.POST, environment.getProperty("api.login.url.path")).permitAll().anyRequest()
				.authenticated();
		/*
		 * .and().addFilter(new AuthorizationFilter(authenticationManager(),
		 * environment));
		 */
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}

	/*
	 * @Bean public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity
	 * serverHttpSecurity) { return
	 * serverHttpSecurity.csrf().disable().formLogin().and().authorizeExchange().
	 * pathMatchers("/order/**")
	 * .hasAuthority("orderManager").anyExchange().authenticated().and().build(); }
	 */
}
