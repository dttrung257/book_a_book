package com.uet.book_a_book.email;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailSenderService {

	private @Autowired JavaMailSender mailSender;
	
	public void sendEmail(String toEmail, String body) {
		try {
		MimeMessage mailMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, "UTF-8");
		messageHelper.setFrom(new InternetAddress("noreply@bookabook.com"));
		messageHelper.setTo(toEmail);
		messageHelper.setSubject("[BookABook] Please verify your email address.");
		messageHelper.setText(body, true);
		mailSender.send(mailMessage);
		} catch (MessagingException e) {
			log.error("Fail to send email", e);
			throw new IllegalStateException("Fail to send email");
		}
	}
	
	public String buildEmail(String email, String fullName, String verificationCode) {
		return "<div>"
				+ "<b style=\"color:black\">Dear, " + fullName + "</b>\n"
				+ "<p style=\"color:black\">Almost done! To complete your BookABook sign up, we just need to verify your email address:\n"
				+ "<a href=\"#\" target=\"_blank\">" + email + "</a>\n"
				+ "</p>\n"
				+ "<p style=\"color:black\">Your verification code: \n"
				+ verificationCode
				+ "</p>\n"
				+ "<p style=\"color:black\">Once verified, you can start making purchases at bookabook shop.</p>"
				+ "</div>";
	}
	
	public String generateVerificationCode() {
		Random random = new Random();
		String code = String.valueOf(1000000 + random.nextInt(9000000));
		return code;
	}
}
