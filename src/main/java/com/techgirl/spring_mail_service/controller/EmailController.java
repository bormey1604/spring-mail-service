package com.techgirl.spring_mail_service.controller;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class EmailController {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender mailSender;

    public EmailController(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @GetMapping("/send-email")
    public String sendEmail(@RequestParam String receiver){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(sender);
        message.setTo(receiver);
        message.setSubject("Hello World");
        message.setText("Welcome to Tech Girl");

        mailSender.send(message);

        return "Email successfully sent";
    }

    @PostMapping("/send-email-with-attachment")
    public String sendEmailWithAttachment(@RequestParam String receiver,
                                          @RequestParam String subject,
                                          @RequestParam String messageBody,
                                          @RequestParam("file") MultipartFile file) throws MessagingException, IOException, MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(sender);
        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setText(messageBody);

        helper.addAttachment(file.getOriginalFilename(), file);

        mailSender.send(mimeMessage);

        return "Email with attachment sent successfully!";
    }
}
