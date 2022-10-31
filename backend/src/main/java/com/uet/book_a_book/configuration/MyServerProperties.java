package com.uet.book_a_book.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "my.server")
@Getter
@Setter
public class MyServerProperties {
	private String secretKey;
	private String mailcheckApiKey;
}
