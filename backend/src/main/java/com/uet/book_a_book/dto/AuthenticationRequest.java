package com.uet.book_a_book.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
	@Email(message = "Email is not valid.")
	private String email;
	
	@NotBlank(message = "Password cannot be blank.")
	@Size(min = 8, max = 100, message = "Password should have at least 8 characters.")
	private String password;
}
