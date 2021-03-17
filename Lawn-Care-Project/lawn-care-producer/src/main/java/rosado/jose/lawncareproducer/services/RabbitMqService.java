package rosado.jose.lawncareproducer.services;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RabbitMqService {

    @Autowired
    AmqpTemplate rabbitTemplate;

    @Value("${RABBITMQ_EXCHANGE}")
    private String exchange;

    @Value("${RABBITMQ_ROUTING_KEY}")
    private String routingKey;

    public void send(Map<String, String> payload) {
//        String customMessage = "This is the message: " + message;
        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
    }

}
