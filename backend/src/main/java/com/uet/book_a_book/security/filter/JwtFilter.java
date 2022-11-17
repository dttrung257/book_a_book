package com.uet.book_a_book.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.repository.UserRepository;
import com.uet.book_a_book.security.jwt.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer") || header.length() <= 8) {
			filterChain.doFilter(request, response);
			return;
		}
		String jwtToken = header.substring(7).trim();

		if (!jwtUtil.verifyJwtToken(jwtToken)) {
			filterChain.doFilter(request, response);
			return;
		}
		String email = jwtUtil.getEmailFromToken(jwtToken);
		AppUser user = userRepository.findByEmail(email).orElse(null);
		if (user == null) {
			filterChain.doFilter(request, response);
			return;
		}
		if (!user.isEmailVerified()) {
			filterChain.doFilter(request, response);
			return;
		}
		if (user.isLocked()) {
			filterChain.doFilter(request, response);
			return;
		}
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null,
				user.getAuthorities());
		token.setDetails(new WebAuthenticationDetails(request));
		SecurityContextHolder.getContext().setAuthentication(token);
		filterChain.doFilter(request, response);
	}
}
