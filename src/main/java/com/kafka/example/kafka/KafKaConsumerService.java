package com.kafka.example.kafka;


import com.kafka.example.models.User;
import com.kafka.example.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class KafKaConsumerService {

    private final Logger logger = LoggerFactory.getLogger(KafKaConsumerService.class);

    @Autowired
    UserService userService;

    @KafkaListener(topics = "${user.topic.name}",
            groupId = "${user.topic.group.id}",
            containerFactory = "userKafkaListenerContainerFactory")
    public void processUser(User user){
        logger.info("Received content = '{}'", user);
        try{
            User newUser = this.userService.addUser(user);
            logger.info(String.format("User created -> %s", newUser));

        }catch (Exception e){
            logger.error("An error occurred! '{}'", e.getMessage());
        }

    }


}
