package com.uet.book_a_book.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUser {
	@NotBlank(message = "First name field cannot be blank")
	private String firstName;
	
	@NotBlank(message = "Last name field cannot be blank")
	private String lastName;
	
	@NotBlank(message = "Gender field cannot be blank")
	private String gender;
	
	@Pattern(regexp = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}", message = "Phone number field is invalid")
	private String phoneNumber;
	
	@NotBlank(message = "Address field cannot be blank")
	private String address;
	
	@NotBlank(message = "Avatar link field cannot be blank")
	private String avatar;
}
