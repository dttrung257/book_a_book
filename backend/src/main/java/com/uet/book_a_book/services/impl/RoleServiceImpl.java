package com.uet.book_a_book.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.models.Role;
import com.uet.book_a_book.repositories.RoleRepository;
import com.uet.book_a_book.services.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
	private final RoleRepository roleRepository;

	@Override
	public Role findByRoleName(String roleName) {
		return roleRepository.findByRoleName(roleName);
	}

	@Override
	public Role save(Role role) {
		return roleRepository.save(role);
	}
	
}
