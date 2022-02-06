package com.ldap.authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LDAPController {
	
	@GetMapping("/hello")
	public String sayHello() {
		return "Hi, you are Successfully Authenticated with LDAP user";
	}

}
