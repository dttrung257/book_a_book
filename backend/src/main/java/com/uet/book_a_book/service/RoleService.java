package com.uet.book_a_book.service;

import com.uet.book_a_book.model.Role;

public interface RoleService {
	Role findByRoleName(String roleName);
	Role save(Role role);
}
