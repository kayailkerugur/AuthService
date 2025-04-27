package com.kayailker.authservice.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("seninmailadresin@gmail.com"); // Buraya kendi email adresini yaz
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendForgotPasswordEmail(String to, String fullName, String code) {
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("code", code);

        String htmlContent = templateEngine.process("forgot-password", context);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Şifre Sıfırlama Talebi");
            helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


        mailSender.send(message);
    }

    public void sendVerificationEmail(String to, String fullName, String verificationCode) throws MessagingException {
        Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("verificationCode", verificationCode);

        String htmlContent = templateEngine.process("verify-email", context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Email Doğrulama Kodu");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
