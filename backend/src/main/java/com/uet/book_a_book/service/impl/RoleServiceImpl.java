package com.uet.book_a_book.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.model.Role;
import com.uet.book_a_book.repository.RoleRepository;
import com.uet.book_a_book.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role findByRoleName(String roleName) {
		return roleRepository.findByRoleName(roleName);
	}

	@Override
	public Role save(Role role) {
		return roleRepository.save(role);
	}
	
}
