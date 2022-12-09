package com.uet.book_a_book.security.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class StaticContentFilter implements Filter {

	private List<String> fileExtensions = Arrays.asList("html", "js", "json", "csv", "css", "png", "svg", "eot", "ttf",
			"woff", "appcache", "jpg", "jpeg", "gif", "ico");

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String path = request.getServletPath();

		boolean isApi = path.startsWith("/api");
		boolean isResourceFile = !isApi && fileExtensions.stream().anyMatch(path::contains);

		if (isApi) {
			chain.doFilter(request, response);
		} else if (isResourceFile) {
			resourceToResponse("static" + path, response);
		} else {
			resourceToResponse("static/index.html", response);
		}
	}

	private void resourceToResponse(String resourcePath, HttpServletResponse response) throws IOException {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);

		if (inputStream == null) {
			response.sendError(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
			return;
		}
		if (resourcePath.endsWith(".html")) {
            response.setContentType("text/html");
        }
        if (resourcePath.endsWith(".css")) {
            response.setContentType("text/css");
        }
        if (resourcePath.endsWith(".js")) {
            response.setContentType("text/javascript");
        }

		inputStream.transferTo(response.getOutputStream());
	}
}
