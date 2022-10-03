package com.uet.book_a_book.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "my.server.security")
@Getter
@Setter
public class MyServerProperties {
	private String secretKey;
}
