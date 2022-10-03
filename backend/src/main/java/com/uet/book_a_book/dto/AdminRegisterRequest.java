package com.uet.book_a_book.dto;

import java.util.HashSet;
import java.util.Set;

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
public class AdminRegisterRequest {
	@NotBlank(message = "First name can not be blank.")
	private String firstName;
	
	@NotBlank(message = "Last name can not be blank.")
	private String lastName;
	
	@Email(message = "Email is not valid.")
	private String email;
	
	@NotBlank(message = "Password can not be blank.")
	@Size(min = 8, max = 100, message = "Password should have at least 8 characters.")
	private String password;
	
	private Set<String> roles = new HashSet<>();
}
