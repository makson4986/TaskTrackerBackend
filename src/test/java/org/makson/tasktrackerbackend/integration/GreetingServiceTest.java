package org.makson.tasktrackerbackend.integration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.makson.tasktrackerbackend.dto.GreetingDto;
import org.makson.tasktrackerbackend.services.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Collections;

@SpringBootTest
@ActiveProfiles("test")
public class GreetingServiceTest extends IntegrationTestBase {
    final GreetingService greetingService;
    final ConsumerFactory<String, GreetingDto> consumerFactory;

    @Autowired
    GreetingServiceTest(GreetingService greetingService, ConsumerFactory<String, GreetingDto> consumerFactory) {
        this.greetingService = greetingService;
        this.consumerFactory = consumerFactory;
    }


    @Test
    @DisplayName("The greeting should be successfully sent to the topic and received by the consumer")
    void greetingShouldSentToTopic(@Value("${spring.kafka.topics.greeting}") String greetingTopic) {
        String email = "test@test.test";

        try (var consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(Collections.singleton(greetingTopic));
            greetingService.sendGreeting(email);
            ConsumerRecord<String, GreetingDto> greeting = KafkaTestUtils.getSingleRecord(consumer, greetingTopic, Duration.ofSeconds(10));
            Assertions.assertEquals(email, greeting.value().recipientEmail());
        }
    }
}
