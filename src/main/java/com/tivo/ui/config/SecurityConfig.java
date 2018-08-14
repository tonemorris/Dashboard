/**
 * 
 */
package com.tivo.ui.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Import(CustomAuthenticationProvider.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	@Value("${spring.queries.users-query}")
	private String usersQuery;
	
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;
	
	@Autowired
	private CustomAuthenticationProvider authProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// require all requests to be authenticated except for the resources
		http.authorizeRequests().antMatchers("/javax.faces.resource/**", "/createUser.xhtml").permitAll().anyRequest()
				.authenticated();
		// login
		http.formLogin().loginPage("/login.xhtml").permitAll().failureUrl("/login.xhtml?error=true");
		// logout
		http.logout().logoutSuccessUrl("/login.xhtml");
		// not needed as JSF 2.2 is implicitly protected against CSRF
		http.csrf().disable();
	}

//	@Override
//	public void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.
//		jdbcAuthentication()
//			.usersByUsernameQuery(usersQuery)
//			.authoritiesByUsernameQuery(rolesQuery)
//			.dataSource(dataSource)
//			.passwordEncoder(passwordencoder());
//	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}

	@Bean(name = "passwordEncoder")
	public PasswordEncoder passwordencoder() {
		return new BCryptPasswordEncoder();
	}
}
