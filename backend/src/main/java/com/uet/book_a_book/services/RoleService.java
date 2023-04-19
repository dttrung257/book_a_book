package com.uet.book_a_book.services;

import com.uet.book_a_book.models.Role;

public interface RoleService {
	Role findByRoleName(String roleName);

	Role save(Role role);
}
