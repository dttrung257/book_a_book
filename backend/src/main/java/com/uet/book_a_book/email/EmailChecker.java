package com.uet.book_a_book.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailChecker {
	private IsSmtpChecker is_smtp_valid;
	private IsMxFound is_mx_found;
	private IsValidFormat is_valid_format;
}

@Getter
@Setter
class IsSmtpChecker {
	private boolean value;
	private String text;
}

@Getter
@Setter
class IsMxFound {
	private boolean value;
	private String text;
}

@Getter
@Setter
class IsValidFormat {
	private boolean value;
	private String text;
}
