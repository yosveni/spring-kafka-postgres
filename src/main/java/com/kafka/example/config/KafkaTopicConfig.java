package com.kafka.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;


    @Value(value = "${general.topic.name}")
    private String topicName;

    @Value(value = "${user.topic.name}")
    private String userTopicName;

    @Bean
    public NewTopic generalTopic(){
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userTopic(){
        return TopicBuilder.name(userTopicName)
                .partitions(1)
                .replicas(1)
                .build();
    }


}
