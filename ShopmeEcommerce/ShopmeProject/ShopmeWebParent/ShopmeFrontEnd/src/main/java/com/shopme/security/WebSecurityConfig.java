package com.shopme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.shopme.security.oauth.CustomerOAuth2UserService;
import com.shopme.security.oauth.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private CustomerOAuth2UserService oAuth2UserService;

	@Autowired
	private OAuth2LoginSuccessHandler oauth2LoginHandler;

	@Autowired
	private DatabaseLoginSuccessHandler databaseLoginHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();

	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/account_details", "/update_account_details", "/orders/**", "/cart",
						"/address_book/**", "/place_order", "reviews/**", "process_paypal_order", "/write_review/**",
						"/post_review", "/ask_question/**", "/post_question/**", "/customer/questions/**")
				.authenticated().requestMatchers("/images/**", "/js/**", "/webjars/**").permitAll().anyRequest()
				.permitAll())
				.formLogin(formLogin -> formLogin.loginPage("/login").usernameParameter("email")
						.successHandler(databaseLoginHandler).permitAll())
				.oauth2Login(oauth2Login -> oauth2Login.loginPage("/login")
						.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
						.successHandler(oauth2LoginHandler))
				.rememberMe(rememberMe -> rememberMe.key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
						.tokenValiditySeconds(14 * 24 * 60 * 60))
				.sessionManagement(
						sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
				.logout(logout -> logout.permitAll());

		return http.build();
	}

}
