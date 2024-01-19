package com.link.backup4j.app.services.impl;

import com.link.backup4j.app.dto.EmailDetails;
import com.link.backup4j.app.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;


@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    private static final String SUCCESS_MESSAGE = "Mail Sent Successfully...";
    private static final String ERROR_MESSAGE = "Error while Sending Mail";
    private static final String ATTACHMENT_ERROR_MESSAGE = "Error while sending mail with attachment!!!";

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String sendSimpleMail(EmailDetails details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(Objects.requireNonNull(details).getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return SUCCESS_MESSAGE;
        } catch (MailException e) {
            log.error("Error sending simple mail: {}", e.getMessage());
            return ERROR_MESSAGE;
        }
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details) {
        try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
                mimeMessageHelper.setFrom(sender);
                mimeMessageHelper.setTo(Objects.requireNonNull(details).getRecipient());
                mimeMessageHelper.setText(details.getMsgBody());
                mimeMessageHelper.setSubject(details.getSubject());

                FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));

                mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);

                javaMailSender.send(mimeMessage);
                log.debug(SUCCESS_MESSAGE);
                return SUCCESS_MESSAGE;
        } catch (MessagingException | MailException e) {
            log.error("Error sending mail with attachment: {}", e.getMessage());
            return ATTACHMENT_ERROR_MESSAGE;
        }
    }
}
