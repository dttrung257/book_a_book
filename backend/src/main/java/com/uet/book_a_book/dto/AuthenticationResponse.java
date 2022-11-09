package com.uet.book_a_book.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
	private String avatar;
	private String firstName;
	private String lastName;
	private String accessToken;
	private Collection<? extends GrantedAuthority> authorities;
}
