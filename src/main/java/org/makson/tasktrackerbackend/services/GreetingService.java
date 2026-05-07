package org.makson.tasktrackerbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.makson.tasktrackerbackend.dto.GreetingDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GreetingService {
    private final KafkaService kafkaService;
    @Value("${spring.kafka.topic-name}")
    private String topicName;

    public void sendGreeting(String email) {
        GreetingDto greeting = createGreeting(email);
        kafkaService.send(topicName, greeting);
        log.info("A welcome email has been sent to the user at {}.", email);
    }

    private GreetingDto createGreeting(String email) {
        return new GreetingDto(
                email,
                "Добро пожаловать в Task Tracker!",
                """
                        Здравствуйте, %s!
                        
                        Мы рады, что вы присоединились к Task Tracker. 
                        Наша цель — помочь вам упростить работу с задачами.
                        """.formatted(email)
        );
    }
}
