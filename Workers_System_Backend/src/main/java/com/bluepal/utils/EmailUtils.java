package com.bluepal.utils;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {
	@Autowired
	private JavaMailSender mailSender;
	
	public  boolean sendEmail(File file) {
		boolean status=false;
		try {
			MimeMessage msg =mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(msg, true);
			helper.setTo("shoebsayyad41@gmail.com");
			helper.setSubject("Worker Report");
			helper.setText("<h2> Please Download Worker Report</h2>", true);
			
			helper.addAttachment(file.getName(), file);
			
			mailSender.send(msg);
			status=true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

}
