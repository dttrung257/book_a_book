package com.uet.book_a_book.email;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.uet.book_a_book.exception.account.EmailSendingErrorException;

@Service
public class EmailSenderService {

	private @Autowired JavaMailSender mailSender;
	
	public void sendEmail(String toEmail, String body) {
		try {
		MimeMessage mailMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, "UTF-8");
		messageHelper.setFrom("BookABook <noreply@bookabook.com>");
		messageHelper.setTo(toEmail);
		messageHelper.setSubject("[BookABook] Please verify your email address.");
		messageHelper.setText(body, true);
		mailSender.send(mailMessage);
		} catch (MessagingException e) {
			throw new EmailSendingErrorException("Fail to send email");
		}
	}
	
	public String buildEmailVerificationAccount(String email, String fullName, String verificationCode) {
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
	
	public String buildEmailForgotPassword(String fullName, String verificationCode) {
		return "<div>"
				+ "<b style=\"color:black\">Dear, " + fullName + "</b>\n"
				+ "<p style=\"color:black\">The confirmation code to change your password is: \n"
				+ verificationCode
				+ "</div>";
	}
	
	public String generateVerificationCode() {
		Random random = new Random();
		String code = String.valueOf(1000000 + random.nextInt(9000000));
		return code;
	}
}
