package ru.seredavin.gitlicenseparser.transport;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
@AllArgsConstructor
public class Producer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public ListenableFuture<SendResult<String, String>> sendMessage(String topic, String key, String message) {
        return kafkaTemplate.send(topic, key, message);
    }
}
