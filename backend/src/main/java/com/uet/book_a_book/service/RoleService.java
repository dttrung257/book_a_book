package com.uet.book_a_book.service;

import java.util.List;

import com.uet.book_a_book.domain.Role;

public interface RoleService {
	List<Role> findAll();
	Role findByRoleName(String roleName);
	Role save(Role role);
}
