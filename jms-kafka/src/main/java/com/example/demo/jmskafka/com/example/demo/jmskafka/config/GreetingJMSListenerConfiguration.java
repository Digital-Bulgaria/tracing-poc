package com.example.demo.jmskafka.com.example.demo.jmskafka.config;

import com.example.demo.jmskafka.BirthdayService;
import com.example.demo.jmskafka.com.example.demo.jmskafka.listener.GreetingMessageListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableConfigurationProperties({JMSConfigurationProperties.class})
public class GreetingJMSListenerConfiguration implements JmsListenerConfigurer {

  private final JMSConfigurationProperties jmsConfigurationProperties;
  private final ObjectMapper objectMapper;
  private final BirthdayService birthdayService;

  public GreetingJMSListenerConfiguration(final JMSConfigurationProperties jmsConfigProperties,
      final ObjectMapper objectMapper,
      final BirthdayService birthdayService) {

    this.jmsConfigurationProperties = jmsConfigProperties;
    this.objectMapper = objectMapper;
    this.birthdayService = birthdayService;
  }

  @Override
  public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {

    SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
    endpoint.setId("greetings-endpoint-poc");
    endpoint.setDestination(jmsConfigurationProperties.getDestination());
    endpoint.setMessageListener(new GreetingMessageListener(objectMapper, birthdayService));

    registrar.registerEndpoint(endpoint);
  }
}
