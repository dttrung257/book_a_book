package com.uet.book_a_book.repository;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uet.book_a_book.entity.Role;

@Configuration
public interface RoleRepository extends JpaRepository<Role, Long> {
	@Query("SELECT r FROM Role r")
	List<Role> findAll();
	
	@Query("SELECT r FROM Role r WHERE r.roleName = :roleName")
	Role findByRoleName(@Param("roleName") String roleName);
}
