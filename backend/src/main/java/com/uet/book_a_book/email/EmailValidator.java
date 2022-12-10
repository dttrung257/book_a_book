package com.uet.book_a_book.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailValidator {
	@Value("${my.server.mailcheckApiKey}")
	private String apikey;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public boolean checkEmailExists(String email) {
		String url = "https://emailvalidation.abstractapi.com/v1/?api_key="+ apikey +"&email=" + email;
		EmailChecker checker = restTemplate.getForObject(url, EmailChecker.class);
		if (checker != null && checker.getIs_valid_format().isValue()
				&& checker.getIs_mx_found().isValue()
				&& checker.getIs_smtp_valid().isValue()) {
			return true;
		}
		return false;
	}
}
