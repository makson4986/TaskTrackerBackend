package org.makson.tasktrackerbackend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.topics.greeting}")
    private String greetingTopic;

    @Bean
    public NewTopic greetingTopic() {
        return TopicBuilder
                .name(greetingTopic)
                .partitions(3)
                .replicas(3)
                .build();
    }
}
