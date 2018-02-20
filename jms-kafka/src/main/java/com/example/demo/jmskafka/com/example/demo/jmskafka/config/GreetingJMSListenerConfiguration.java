package com.example.demo.jmskafka.com.example.demo.jmskafka.config;

import com.example.demo.jmskafka.BirthdayService;
import com.example.demo.jmskafka.com.example.demo.jmskafka.listener.GreetingMessageListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class GreetingJMSListenerConfiguration implements JmsListenerConfigurer {

  private final ObjectMapper objectMapper;
  private final BirthdayService birthdayService;
  private final Tracer tracer;

  public GreetingJMSListenerConfiguration(
      final ObjectMapper objectMapper,
      final BirthdayService birthdayService,
      final Tracer tracer) {

    this.objectMapper = objectMapper;
    this.birthdayService = birthdayService;
    this.tracer = tracer;
  }

  @Override
  public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {

    SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
    //
    endpoint.setId("greetings-endpoint-poc");
    endpoint.setDestination("tracing_poc_greetings");

    endpoint.setMessageListener(new GreetingMessageListener(objectMapper, birthdayService, tracer));

    registrar.registerEndpoint(endpoint);
  }
}
