package com.uet.book_a_book.email;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailValidator {
	public boolean validateEmail(String email) {
		try {
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			return true;
		} catch (AddressException e) {
			log.error(String.format("Email %s does not exists on the Internet", email));
		}
		return false;
	}
}
