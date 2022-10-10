package com.uet.book_a_book.configuration;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.uet.book_a_book.domain.AppUser;
import com.uet.book_a_book.domain.Const;
import com.uet.book_a_book.domain.Role;
import com.uet.book_a_book.service.RoleService;
import com.uet.book_a_book.service.UserSevice;

@Configuration
public class DatabaseResourceTest {
	private @Autowired UserSevice userSevice;
	private @Autowired RoleService roleService;
	private @Autowired PasswordEncoder passwordEncoder;

	@Bean
	public void initTestDatabase() {
		roleService.save(new Role(Const.ROLE_ROOT_ADMIN));
		roleService.save(new Role(Const.ROLE_ADMIN));
		roleService.save(new Role(Const.ROLE_REPO_MANAGER));
		roleService.save(new Role(Const.ROLE_CUSTOMER_CARE_STAFF));
		roleService.save(new Role(Const.ROLE_USER));

		Role rootAdmin = roleService.findByRoleName("ROOT_ADMIN");
//		Role admin = roleService.findByRoleName("ADMIN");
//		Role manager = roleService.findByRoleName("REPO_MANAGER");
		//Role user = roleService.findByRoleName("USER");

		userSevice.save(new AppUser("Trung", "Dang Thanh", "dttrung257@gmail.com", null, passwordEncoder.encode("123456789"), Const.GENDER_MALE,
				"0904225702", "Viet Nam", "#", new Date(), null, false, true, null, Set.of(rootAdmin)));
	}
}
