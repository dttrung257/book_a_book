package com.uet.book_a_book.dto.user;

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
public class ResetPassword {
	@Email(message = "email field is not valid")
	private String email;
	
	@NotBlank(message = "resetToken field is mandatory")
	private String resetToken;
	
	@NotBlank(message = "password field cannot be blank")
	@Size(min = 8, max = 100, message = "password field should have at least 8 characters")
	private String newPassword;
}
