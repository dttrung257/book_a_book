package com.uet.book_a_book.dto.order;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.uet.book_a_book.entity.constant.Const;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewOrder {
	@NotBlank(message = "address field is mandatory")
	private String address;
	
	@NotNull(message = "phoneNumber field is mandatory")
	@Pattern(regexp = Const.PHONE_NUMBER_REGEX, message = "Phone number field is invalid")
	private String phoneNumber;
	
	@NotEmpty(message = "orderdetails field is mandatory")
	@Valid
	private List<NewOrderdetail> orderdetails;
}
