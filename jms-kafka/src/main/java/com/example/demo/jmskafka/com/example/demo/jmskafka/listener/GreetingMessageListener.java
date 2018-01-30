package com.example.demo.jmskafka.com.example.demo.jmskafka.listener;

import com.example.demo.jmskafka.com.example.demo.jmskafka.domain.Greeting;
import com.example.demo.jmskafka.com.example.demo.jmskafka.kafka.KafkaMessage;
import com.example.demo.jmskafka.com.example.demo.jmskafka.kafka.KafkaPayload;
import com.example.demo.jmskafka.com.example.demo.jmskafka.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class GreetingMessageListener implements MessageListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(GreetingMessageListener.class);

  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String kafkaTopic;


  public GreetingMessageListener(
      final ObjectMapper objectMapper,
      final KafkaTemplate<String, String> kafkaTemplate,
      final String kafkaTopic
  ) {

    this.objectMapper = objectMapper;
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaTopic = kafkaTopic;
  }

  public void onMessage(Message message) {

    TextMessage textMessage = (TextMessage) message;
    KafkaMessage<Greeting> kafkaMessage = null;
    UUID messageID = UUID.randomUUID();
    UUID messageKey = UUID.randomUUID();
    try {
      LOGGER.debug("JMS message received type: {}, text: {}", textMessage.getJMSType(), textMessage.getText());

      Greeting greeting = objectMapper.readValue(textMessage.getText(), Greeting.class);
      LOGGER.debug("Greeting JSON object: {}" , greeting);

      KafkaPayload<Greeting> kafkaPayload = new KafkaPayload<>(0, greeting);
      ZonedDateTime date = ZonedDateTime.now(ZoneOffset.UTC);
      kafkaMessage = new KafkaMessage<>(messageID, messageKey.toString(), date, Constants.MESSAGE_TYPE_BIRTHDAY,
          kafkaPayload);
    }
    catch (JMSException e) {
      LOGGER.error("Error reading JMS message", e);
    }
    catch (IOException ioEx) {
      LOGGER.error("Error parsing JSON message", ioEx);
    }

    String stringMessage;
    try {
      stringMessage = objectMapper.writeValueAsString(kafkaMessage);
    }
    catch (JsonProcessingException e) {
      LOGGER.error("Error converting to JSON string for kafka message with ID {}, key: {}", messageID, messageKey, e);
      return;
    }

    kafkaTemplate.send(kafkaTopic, messageKey.toString(), stringMessage);
    LOGGER.debug("Sent Kafka message: {}", stringMessage);
  }

}
