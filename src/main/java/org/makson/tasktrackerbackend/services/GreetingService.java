package org.makson.tasktrackerbackend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.makson.tasktrackerbackend.dto.GreetingDto;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GreetingService {
    private final KafkaService kafkaService;

    public void sendGreeting(String email) {
        kafkaService.publishGreetingCreatedEvent(createGreeting(email));
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
