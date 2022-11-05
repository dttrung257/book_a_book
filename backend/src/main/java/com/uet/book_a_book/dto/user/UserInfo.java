package com.uet.book_a_book.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private String phoneNumber;
	private String address;
	private String avatar;
}
