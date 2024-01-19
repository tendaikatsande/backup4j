package com.link.backup4j.app.services;

import com.link.backup4j.app.dto.EmailDetails;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);

    // Method 2
    // To send an email with attachment
    String
    sendMailWithAttachment(EmailDetails details);
}
