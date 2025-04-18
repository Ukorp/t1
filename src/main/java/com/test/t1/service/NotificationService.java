package com.test.t1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${t1.mail.receiver}")
    private String receiver;

    public void send(Long id, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(receiver);
        message.setSubject("Изменение статуса задания");
        message.setText(String.format("Статус задания id %d изменён: %s", id, status));
        mailSender.send(message);
    }
}
