package org.makson.tasktrackerbackend.services;

import lombok.RequiredArgsConstructor;
import org.makson.tasktrackerbackend.dto.GreetingDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {
    @Value("${spring.kafka.topics.greeting}")
    private String greetingTopic;
    private final KafkaTemplate<String, GreetingDto> kafkaTemplate;

    public void publishGreetingCreatedEvent(GreetingDto greeting) {
        kafkaTemplate.send(greetingTopic, greeting.recipientEmail(), greeting);
    }
}