package com.narwal.bookingservice.service;

import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration configuration;

    @Value("${spring.mail.username}")
    String from;

    public String sendMail(String to) {
        String response;
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            ClassPathResource pdf = new ClassPathResource("C:\\Users\\anarw\\OnxeDrive\\Desktop\\CG-Assignments\\RabbitMQ-Assingments\\booking-service\\src\\main\\resources\\static\\ticket.pdf");
//            InputStream inputStream = new FileInputStream("");
            FileSystemResource pdf = new FileSystemResource(new File("C:\\Users\\anarw\\OneDrive\\Desktop\\CG-Assignments\\RabbitMQ-Assingments\\booking-service\\src\\main\\resources\\static\\ticket.pdf"));
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject("AN Railways e-Ticket Slip");
            helper.setText(getMessage());
            helper.addAttachment("ticket.pdf", pdf);
            mailSender.send(message);
            response = "Email has been sent to :" + to;
        } catch (MessagingException e) {
            response = "Email send failure to :" + to;
            e.printStackTrace();
        }
        return response;
    }

    private String getMessage() {
        return "Dear Customer\n\nThank you for booking your ticket with us. Wish you a safe and sound journey.\n\nThank you.";
    }

}
