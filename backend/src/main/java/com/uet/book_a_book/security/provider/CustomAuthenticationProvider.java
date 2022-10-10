package com.uet.book_a_book.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.uet.book_a_book.domain.AppUser;
import com.uet.book_a_book.repository.UserRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();
		AppUser user = userRepository.findByUserEmail(email).orElse(null);
		if (user == null) {
			throw new UsernameNotFoundException("Not found user with email: " + email);
		}
		if (!user.isEmailVerified()) {
			throw new IllegalStateException("Account not activated");
		}
		if (user.isLocked()) {
			throw new IllegalStateException("Account is locked");
		}
		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		throw new BadCredentialsException("Wrong password");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}
}
