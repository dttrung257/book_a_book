package com.uet.book_a_book.dtos.user;

import javax.validation.constraints.NotBlank;

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
	private String phoneNumber;
	private String address;
	private String avatar;
}
