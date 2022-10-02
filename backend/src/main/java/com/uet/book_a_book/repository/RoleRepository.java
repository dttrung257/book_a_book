package com.uet.book_a_book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uet.book_a_book.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRoleName(String roleName);
}
