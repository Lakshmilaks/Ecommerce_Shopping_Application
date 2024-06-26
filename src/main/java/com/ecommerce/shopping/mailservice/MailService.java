package com.ecommerce.shopping.mailservice;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailService {

	private final JavaMailSender javaMailSender;
	
	public void sendMail(MessageData messageData) throws MessagingException{
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,true);
		helper.setTo(messageData.getTo());
		helper.setSubject(messageData.getSubject());
		helper.setSentDate(messageData.getSentDate());
		helper.setText(messageData.getText());
		
		javaMailSender.send(message);
	}
}
