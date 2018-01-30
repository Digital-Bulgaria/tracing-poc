package com.example.demo.jmskafka.com.example.demo.jmskafka.config;

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
  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String kafkaTopic;

  public GreetingJMSListenerConfiguration(final JMSConfigurationProperties jmsConfigProperties,
      final ObjectMapper objectMapper,
      final KafkaTemplate<String, String> kafkaTemplate,
      @Value("${kafka.topics.greetings}") String kafkaTopic) {

    this.jmsConfigurationProperties = jmsConfigProperties;
    this.objectMapper = objectMapper;
    this.kafkaTemplate = kafkaTemplate;
    this.kafkaTopic = kafkaTopic;
  }

  @Override
  public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {

    SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
    endpoint.setId("greetings-endpoint-poc");
    endpoint.setDestination(jmsConfigurationProperties.getDestination());
    endpoint.setMessageListener(new GreetingMessageListener(objectMapper, kafkaTemplate, kafkaTopic));

    registrar.registerEndpoint(endpoint);
  }
}
