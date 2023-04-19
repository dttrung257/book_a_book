package com.uet.book_a_book.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
	private final String avatar;
	private final String firstName;
	private final String lastName;
	private final String accessToken;
	private final String authority;
}
