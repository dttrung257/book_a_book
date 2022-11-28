package com.uet.book_a_book.service;

import com.uet.book_a_book.dto.AuthenRequest;
import com.uet.book_a_book.dto.AuthenResponse;
import com.uet.book_a_book.dto.RegisterRequest;

public interface AuthenService {
	AuthenResponse signIn(AuthenRequest request);
	void register(RegisterRequest request);
}
