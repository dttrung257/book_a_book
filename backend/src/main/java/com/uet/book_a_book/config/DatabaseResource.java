package com.uet.book_a_book.config;

import java.util.Date;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.uet.book_a_book.models.AppUser;
import com.uet.book_a_book.models.Role;
import com.uet.book_a_book.models.constant.Gender;
import com.uet.book_a_book.models.constant.RoleName;
import com.uet.book_a_book.services.RoleService;
import com.uet.book_a_book.services.UserSevice;

@Configuration
@RequiredArgsConstructor
public class DatabaseResource {
    private final UserSevice userSevice;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

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
