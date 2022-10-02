package com.uet.book_a_book.dto.response;

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
	private String firstName;
	private String lastName;
	private String jwtToken;
	private Collection<? extends GrantedAuthority> authorities;
}
