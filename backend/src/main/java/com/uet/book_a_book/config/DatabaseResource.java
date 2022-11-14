package com.uet.book_a_book.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.uet.book_a_book.entity.AppUser;
import com.uet.book_a_book.entity.Role;
import com.uet.book_a_book.entity.constant.Gender;
import com.uet.book_a_book.entity.constant.RoleName;
import com.uet.book_a_book.service.RoleService;
import com.uet.book_a_book.service.UserSevice;

@Configuration
public class DatabaseResource {
    private @Autowired UserSevice userSevice;
    private @Autowired RoleService roleService;
    private @Autowired PasswordEncoder passwordEncoder;

    @Bean
    public void initTestDatabase() {
        if (roleService.findByRoleName(RoleName.ROLE_ADMIN) == null) {
            roleService.save(new Role(RoleName.ROLE_ADMIN));
        }
        if (roleService.findByRoleName(RoleName.ROLE_USER) == null) {
            roleService.save(new Role(RoleName.ROLE_USER));
        }

        Role roleAdmin = roleService.findByRoleName(RoleName.ROLE_ADMIN);

        if (userSevice.findByEmail("dttrung257@gmail.com") == null) {
            userSevice.save(new AppUser("Trung", "Dang Thanh", "dttrung257@gmail.com", null,
                    passwordEncoder.encode("123456789"), Gender.GENDER_MALE,
                    "0904225702", "Viet Nam", "#", new Date(), null, false, true, roleAdmin));
        }
    }
}
