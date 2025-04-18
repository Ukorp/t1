package com.test.t1.kafka;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = "t1.kafka.producer.enable",
        havingValue = "true",
        matchIfMissing = true)
public class KafkaClientProducer {

    private final KafkaTemplate<Long, String> kafkaTemplate;

    @Value("${t1.kafka.topic.mail-service}")
    private String topic;

    @PostConstruct
    public void init() {
        kafkaTemplate.setDefaultTopic(topic);
    }

    public void setTopic(String topic) {
        kafkaTemplate.setDefaultTopic(topic);
    }

    public void send(Long key, String status) {
        try {
            log.info("Идёт отправка: {} {}", key, status);
            kafkaTemplate.sendDefault(key, status);
            kafkaTemplate.flush();
            log.info("Отправлено");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
