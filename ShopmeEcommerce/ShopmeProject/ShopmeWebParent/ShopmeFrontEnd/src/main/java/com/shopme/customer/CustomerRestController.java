package com.shopme.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class CustomerRestController {

	@Autowired
	CustomerService service;
	
	@PostMapping("/customer/check_unique_email")
	public String checkDuplicateEmail(String email) {
		return service.isEmailUnique(email) ? "OK" : "Duplicated";
	}
}
