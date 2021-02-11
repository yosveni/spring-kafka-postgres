package com.kafka.example.kafka;


import com.kafka.example.models.User;
import com.kafka.example.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class KafKaConsumerService {

    private final Logger logger = LoggerFactory.getLogger(KafKaConsumerService.class);


    @Autowired
    UserService userService;

    @KafkaListener(topics = "${general.topic.name}",
                    groupId = "${general.topic.group.id}")
    public void consume(String message){
        logger.info(String.format("Message recieved -> %s", message));
    }


    @KafkaListener(topics = "${user.topic.name}",
            groupId = "${user.topic.group.id}")
    public void processUser(User user){
        try{
            User newUser = this.userService.addUser(user);
            logger.info(String.format("User created -> %s", newUser));

        }catch (Exception e){
            logger.error("An error occurred! '{}'", e.getMessage());
        }

    }


}
