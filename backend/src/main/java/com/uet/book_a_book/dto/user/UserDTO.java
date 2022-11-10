package com.uet.book_a_book.dto.user;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private String phoneNumber;
	private String address;
	private String avatar;
	@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss", timezone = "GMT+7")
	private Date createdAt;
	@JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss", timezone = "GMT+7")
	private Date updatedAt;
	private boolean locked;
	private boolean emailVerified;
	private String authority;
}
