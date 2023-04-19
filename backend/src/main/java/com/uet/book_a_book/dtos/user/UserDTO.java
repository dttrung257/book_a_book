package com.uet.book_a_book.dtos.user;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uet.book_a_book.models.constant.Const;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private UUID id;
	private String firstName;
	private String lastName;
	private String email;
	private String gender;
	private String phoneNumber;
	private String address;
	private String avatar;
	@JsonFormat(pattern = Const.DEFAULT_DATETIME_FORMAT, timezone = Const.DEFAULT_TIMEZONE)
	private Date createdAt;
	@JsonFormat(pattern = Const.DEFAULT_DATE_FORMAT, timezone = Const.DEFAULT_TIMEZONE)
	private Date updatedAt;
	private boolean locked;
	private boolean emailVerified;
	private String authority;
}
