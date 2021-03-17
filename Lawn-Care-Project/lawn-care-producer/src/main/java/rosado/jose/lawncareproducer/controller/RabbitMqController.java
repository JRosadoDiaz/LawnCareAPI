package rosado.jose.lawncareproducer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rosado.jose.lawncareproducer.services.RabbitMqService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rabbit")
public class RabbitMqController {

    @Autowired
    RabbitMqService rabbitMqService;

    @GetMapping("/producer")
    public ResponseEntity<Object> producer(@RequestParam(value = "message") String message) throws IOException {
        Map<String, String> payload = new HashMap<>();
        payload.put("message", message);
        rabbitMqService.send(payload);
        System.out.println("Sent message: " + message);
        return new ResponseEntity<>("SentMessage: " + message, HttpStatus.OK);
    }
}
