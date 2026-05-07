package org.makson.tasktrackerbackend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    private final String topicName;

    public KafkaConfig(@Value("${spring.kafka.topic-name}") String topicName) {
        this.topicName = topicName;
    }

    @Bean
    public NewTopic greetingTopic() {
        return TopicBuilder
                .name(topicName)
                .build();
    }
}
