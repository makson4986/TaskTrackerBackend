package org.makson.tasktrackerbackend.integration;

import org.apache.kafka.clients.consumer.Consumer;
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
    final String greetingTopicName;

    @Autowired
    GreetingServiceTest(
            GreetingService greetingService,
            ConsumerFactory<String, GreetingDto> consumerFactory,
            @Value("${spring.kafka.topics.greeting}") String greetingTopicName
    ) {
        this.greetingService = greetingService;
        this.consumerFactory = consumerFactory;
        this.greetingTopicName = greetingTopicName;
    }


    @Test
    @DisplayName("The greeting should be successfully sent to the topic and received by the consumer")
    void greetingShouldSentToTopic() {
        String email = "test@test.test";

        try (Consumer<String, GreetingDto> consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(Collections.singleton(greetingTopicName));
            greetingService.sendGreeting(email);
            ConsumerRecord<String, GreetingDto> greeting = KafkaTestUtils.getSingleRecord(consumer, greetingTopicName, Duration.ofSeconds(10));
            Assertions.assertEquals(email, greeting.value().recipientEmail());
        }
    }
}
