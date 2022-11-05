package com.uet.book_a_book.dto.user;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;

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
	private Date createdAt;
	private Date updatedAt;
	private boolean locked;
	private boolean emailVerified;
	private Collection<? extends GrantedAuthority> authorities;
}
