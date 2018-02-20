package com.example.demo.jmskafka.com.example.demo.jmskafka.listener;

import com.example.demo.jmskafka.BirthdayService;
import com.example.demo.jmskafka.domain.Greeting;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

public class GreetingMessageListener implements MessageListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(GreetingMessageListener.class);

  private final ObjectMapper objectMapper;
  private final BirthdayService birthdayService;
  private final Tracer tracer;

  public GreetingMessageListener(
      final ObjectMapper objectMapper,
      final BirthdayService birthdayService,
      final Tracer tracer) {

    this.objectMapper = objectMapper;
    this.birthdayService = birthdayService;
    this.tracer = tracer;
  }

  public void onMessage(Message message) {

    Span span = tracer.createSpan("jms-greeting-received");
    TextMessage textMessage = (TextMessage) message;
    try {
      LOGGER.debug("JMS message received type: {}, text: {}", textMessage.getJMSType(), textMessage.getText());

      Greeting greeting = objectMapper.readValue(textMessage.getText(), Greeting.class);
      LOGGER.debug("Greeting JSON object: {}", greeting);

      try {
        int waitFor = 2000;
        LOGGER.debug("Deliberately waiting for {} millis...", waitFor);
        Thread.sleep(waitFor);
      } catch (InterruptedException e) {
        // do nothing
      }

      birthdayService.sendGreeting(greeting, span);
      tracer.close(span);
    }
    catch (JMSException|IOException e) {
      LOGGER.error("Error reading/parsing JMS message", e);
    }
  }

}
