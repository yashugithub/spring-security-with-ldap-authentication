package com.ldap.authentication.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * LDAP authentication security configurations
 *
 */
@Configuration
public class LDAPSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * Configure the LDAP server information and search details
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.ldapAuthentication()
			.userDnPatterns("cn={0},ou=contractor,ou=people") // Search users by distinguished name. Search Contractors
			.groupSearchBase("ou=people") // search user by groups organizational unit
			.contextSource().url("ldap://localhost:8389/dc=ABC,dc=net") // define ldap server information to connect ldap server with domain details
			.and()				
			.passwordCompare()
			.passwordEncoder(passwordEncoder()) // Password is in clear test, not recommended for production
			.passwordAttribute("userpassword"); // password defined with "userpassword" attribute in ldif file
	}

	/**
	 * Enable the security filters for request to go through the authentication
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin();
	}
	
	/**
	 * This encoder is for testing LDAP credential with cleartext password. 
	 * This is not recommended for production
	 * 
	 * @return
	 * 	PasswordEncoder : NoOpPasswordEncoder
	 */
	@Bean
	public static PasswordEncoder passwordEncoder() {
	    return NoOpPasswordEncoder.getInstance();
	}
}
