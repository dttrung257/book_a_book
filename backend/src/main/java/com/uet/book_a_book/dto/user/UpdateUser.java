package com.uet.book_a_book.dto.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.uet.book_a_book.entity.constant.Const;

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
	
	@Pattern(regexp = Const.PHONE_NUMBER_REGEX, message = "Phone number field is invalid")
	private String phoneNumber;
	
	@NotBlank(message = "address field is mandatory")
	private String address;
	
	@NotBlank(message = "avatar field is mandatory")
	private String avatar;
}
