package com.uet.book_a_book;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.uet.book_a_book.model.AppUser;
import com.uet.book_a_book.model.Const;
import com.uet.book_a_book.model.Role;
import com.uet.book_a_book.service.RoleService;
import com.uet.book_a_book.service.UserSevice;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan(basePackages = "com.uet.book_a_book")
public class BookABookApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BookABookApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserSevice userSevice, RoleService roleService) {
		return args -> {
			roleService.save(new Role(null, Const.ROLE_ADMIN));
			roleService.save(new Role(null, Const.ROLE_MANAGER));
			roleService.save(new Role(null, Const.ROLE_USER));

			Role admin = roleService.findByRoleName("ADMIN");
			Role manager = roleService.findByRoleName("MANAGER");
			Role user = roleService.findByRoleName("USER");

			userSevice.save(new AppUser(null, "Trung", "Dang Thanh", "trungdt@gmail.com", passwordEncoder.encode("123456789"), "MALE",
					"+84911222333", "VietNam", "#", Set.of(admin, manager, user), new Date(), null, false, false));
			userSevice.save(new AppUser(null, "test", "test", "test", passwordEncoder.encode("123456789"), "MALE",
					"+84911222333", "VietNam", "#", Set.of(user), new Date(), null, false, false));
		};
	}
}
