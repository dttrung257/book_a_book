package com.uet.book_a_book.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdValidator implements ConstraintValidator<IdConstraint, String> {

	@Override
	public boolean isValid(String idField, ConstraintValidatorContext context) {
		return idField != null
				&& idField.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
	}

}
