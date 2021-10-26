package com.accountmanagementservice.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class UserAccount {
	@GetMapping(path = "/status/check")
	public String status() {
		return "working";
	}

}
