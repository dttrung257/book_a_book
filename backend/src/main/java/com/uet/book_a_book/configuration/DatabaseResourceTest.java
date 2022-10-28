package com.uet.book_a_book.configuration;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Role;
import com.uet.book_a_book.entity.util.Gender;
import com.uet.book_a_book.entity.util.RoleName;
import com.uet.book_a_book.service.RoleService;
import com.uet.book_a_book.service.UserSevice;

@Configuration
public class DatabaseResourceTest {
	private @Autowired UserSevice userSevice;
	private @Autowired RoleService roleService;
	private @Autowired PasswordEncoder passwordEncoder;

	@Bean
	public void initTestDatabase() {
		roleService.save(new Role(RoleName.ROLE_ADMIN));
		roleService.save(new Role(RoleName.ROLE_USER));

		Role roleAdmin = roleService.findByRoleName("ADMIN");

		userSevice.save(new AppUser("Trung", "Dang Thanh", "dttrung257@gmail.com", null, passwordEncoder.encode("123456789"), Gender.GENDER_MALE,
				"0904225702", "Viet Nam", "#", new Date(), null, false, true, null, roleAdmin));
	}
}
