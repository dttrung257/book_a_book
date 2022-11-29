package com.uet.book_a_book.entity.constant;

public final class Const {
	public static final String DEFAULT_MIN_PRICE = "0";
	public static final String DEFAULT_MAX_PRICE = "100000000000";
	public static final String DEFAULT_PAGE_NUMBER = "0";
	public static final String DEFAULT_PAGE_SIZE = "10";
	public static final int MIN_STAR_NUMBER = 0;
	public static final int MAX_STAR_NUMBER = 5;
	
	public static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
	public static final String DEFAULT_DATETIME_FORMAT = "dd-MM-yyyy hh:mm:ss";
	public static final String DEFAULT_TIMEZONE = "GMT+7";
	
	public static final String PHONE_NUMBER_REGEX = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
	public static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
}
