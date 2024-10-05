package com.link.backup4j.app.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailServerConfig {

    @Value("${spring.mail.host:mail.csam.co.zw}")
    private String mailHost;

    @Value("${spring.mail.port:465}")
    private int mailPort;

    @Value("${spring.mail.username:noreply@csam.co.zw}")
    private String mailUsername;

    @Value("${spring.mail.password:${MAIL_PASSWORD:csam2022}}")
    private String mailPassword;

    @Value("${spring.mail.properties.mail.smtp.auth:true}")
    private String mailSmtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:false}")
    private String mailSmtpStartTlsEnable;

    @Value("${spring.mail.properties.mail.smtp.ssl.enable:true}")
    private String mailSmtpSslEnable;

    @Value("${spring.mail.debug:false}")
    private String mailDebug;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailSmtpStartTlsEnable);
        props.put("mail.smtp.ssl.enable", mailSmtpSslEnable);
        props.put("mail.smtp.ssl.trust", mailHost);
        props.put("mail.debug", mailDebug);

        return mailSender;
    }
}