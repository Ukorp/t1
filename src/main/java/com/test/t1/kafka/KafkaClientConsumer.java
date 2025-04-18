package com.test.t1.kafka;

import com.test.t1.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaClientConsumer {

    private final NotificationService notificationService;

    @KafkaListener(id = "${t1.kafka.group}",
            topics = "${t1.kafka.topic.mail-service}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(@Payload List<String> list,
                        @Header(KafkaHeaders.RECEIVED_KEY) Long key,
                        Acknowledgment ack) {
        log.info("Получены сообщения в kafkaListener размером: {}", list.size());
        try {
            list.forEach(status -> notificationService.send(key, status));
        } finally {
            ack.acknowledge();
            log.info("Отправлен коммит");
        }
    }
}
