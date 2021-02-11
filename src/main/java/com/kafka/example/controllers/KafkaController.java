package com.kafka.example.controllers;


import com.kafka.example.models.User;
import com.kafka.example.kafka.KafKaProducerService;
import com.kafka.example.response.Response;
import com.kafka.example.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    UserService userService;

    private final KafKaProducerService producerService;

    @Autowired
    public KafkaController(KafKaProducerService producerService){
        this.producerService = producerService;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        this.producerService.sendMessage(message);
    }

    @PostMapping(value = "/createUser")
    public void sendMessageToKafkaTopic(
            @RequestParam("userId") long userId,
            @RequestParam("name") String name,
            @RequestParam("age") int age) {

        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setAge(age);

        this.producerService.saveUserLog(user);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Response<User>> addUser(
            @RequestParam("userId") long userId,
            @RequestParam("name") String name,
            @RequestParam("age") int age){
        logger.info(("Process add new user"));
        Response<User> res = new Response<User>();
        User user = new User();
        user.setUserId(userId);
        user.setName(name);
        user.setAge(age);
        try {
            this.producerService.postUser(user);
            res.setData(user);

        }catch (Exception e){
            logger.error("An error occurred! {}", e.getMessage());
        }
       return ResponseEntity.ok(res);

    }

}
