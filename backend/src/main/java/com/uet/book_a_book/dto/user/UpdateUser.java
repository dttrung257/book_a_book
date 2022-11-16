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
	@NotBlank(message = "firstName field is mandatory")
	private String firstName;
	
	@NotBlank(message = "lastName field is mandatory")
	private String lastName;
	
	@NotBlank(message = "gender field is mandatory")
	private String gender;
	
	@Pattern(regexp = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}", message = "Phone number field is invalid")
	private String phoneNumber;
	
	@NotBlank(message = "address field is mandatory")
	private String address;
	
	@NotBlank(message = "avatar field is mandatory")
	private String avatar;
}
