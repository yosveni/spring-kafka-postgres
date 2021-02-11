package com.kafka.example.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.example.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafKaProducerService {

    private final Logger logger = LoggerFactory.getLogger(KafKaProducerService.class);

    //1. General topic with string payload
    @Value(value = "${general.topic.name}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //2. Topic with user object payload
    @Value(value = "${user.topic.name}")
    private String userTopicName;

    @Autowired
    private KafkaTemplate<String, User> userKafkaTemplate;

    public void sendMessage(String message){
        ListenableFuture<SendResult<String, String>> future
                = this.kafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.error("Unable to send message: "+ message, throwable);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                logger.info("Sent message: "+ message +" with offset: "
                + result.getRecordMetadata().offset());

            }
        });
    }

    public void saveUserLog(User user){

        ListenableFuture<SendResult<String, User>> future
                = this.userKafkaTemplate.send(userTopicName, user);

        future.addCallback(new ListenableFutureCallback<SendResult<String, User>>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.error("User created: " + user, throwable);

            }

            @Override
            public void onSuccess(SendResult<String, User> result) {
                logger.info("User created: " + user
                        + "with offset: " + result.getRecordMetadata().offset());

            }
        });

    }

    public void postUser(User user){
        try {
            logger.info("Sending data to kafka = '{}' with topic '{}'", user.getName(), userTopicName);
//            ObjectMapper mapper = new ObjectMapper();
            userKafkaTemplate.send(userTopicName, user);

        }catch (Exception ex){
            logger.error("An error occurred! '{}'", ex.getMessage());

        }
    }




}
