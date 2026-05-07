package org.makson.tasktrackerbackend.services;

import lombok.RequiredArgsConstructor;
import org.makson.tasktrackerbackend.dto.GreetingDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingService {
    private final KafkaService kafkaService;
    @Value("${spring.kafka.topic-name}")
    private String topicName;

    public void sendGreeting(String email) {
        GreetingDto greeting = createGreeting(email);
        kafkaService.send(topicName, greeting);
    }

    private GreetingDto createGreeting(String email) {
        return new GreetingDto(
                email,
                "Добро пожаловать в Task Tracker!",
                """
                        Здравствуйте, %s!
                        
                        Мы рады, что вы присоединились к Task Tracker. 
                        Наша цель — помочь вам упростить работу с задачами.
                        """
        );
    }
}
