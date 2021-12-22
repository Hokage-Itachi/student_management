package com.example.student_management.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String hostEmail;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine springTemplateEngine;

    public MailService(JavaMailSender mailSender, SpringTemplateEngine springTemplateEngine) {
        this.mailSender = mailSender;
        this.springTemplateEngine = springTemplateEngine;
    }

    @Async
    public void sendMail(String recipientEmail, String link, String template) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(hostEmail, "Student management system");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";
        Context context = new Context();
        context.setVariable("returnUrl", link);
        String content = springTemplateEngine.process(template, context);

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
