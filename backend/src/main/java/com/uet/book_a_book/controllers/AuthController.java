package com.uet.book_a_book.controllers;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uet.book_a_book.dtos.AuthRequest;
import com.uet.book_a_book.dtos.AuthResponse;
import com.uet.book_a_book.dtos.RegisterRequest;
import com.uet.book_a_book.services.AuthService;
import com.uet.book_a_book.services.UserSevice;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/authen")
@Validated
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final UserSevice userSevice;
    private final AuthService authenService;

    @PostMapping("/sign_in")
    public ResponseEntity<AuthResponse> signIn(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authenService.signIn(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authenService.register(request);
        return new ResponseEntity<>("You have successfully created an account. Please verify your email!",
                HttpStatus.CREATED);
    }

    @GetMapping("{email}/confirm_verification/{code}")
    public ResponseEntity<String> confirmVerification(
            @PathVariable("email") @Email(message = "email field is not valid") String email,
            @PathVariable("code") @NotBlank(message = "code field cannot be blank") String code) {
        userSevice.confirmEmailVerification(email.trim(), code.trim());
        log.info("Account with email {} activated successfully.", email);
        return ResponseEntity.ok("Account activation successful");
    }

    @GetMapping("/send_email/{email}")
    public ResponseEntity<String> sendEmailVerification(
            @PathVariable("email") @Email(message = "mail field is not valid") String email) {
        userSevice.sendEmailVerification(email.trim());
        log.info("Account with email {} send activation email.", email);
        return ResponseEntity.ok("Send email to " + email + " successfully");
    }
}
