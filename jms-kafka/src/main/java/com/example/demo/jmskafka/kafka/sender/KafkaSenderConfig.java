package com.example.demo.jmskafka.kafka.sender;

import com.example.testsleuth.kafka.KafkaMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Kafka sender configurator
 */
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaSenderConfig {

  private final ObjectMapper objectMapper;
  private final KafkaProperties kafkaProperties;

  public KafkaSenderConfig(ObjectMapper objectMapper, KafkaProperties kafkaProperties) {

    this.objectMapper = objectMapper;
    this.kafkaProperties = kafkaProperties;
  }

  @Bean
  public KafkaTemplate<String, KafkaMessage> kafkaTemplate() {
    Map<String, Object> props = kafkaProperties.buildProducerProperties();

    return new KafkaTemplate<>(
        new DefaultKafkaProducerFactory<String, KafkaMessage>(props, new StringSerializer(),
            new JsonSerializer<>(objectMapper)));
  }
}