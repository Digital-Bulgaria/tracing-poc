package com.example.demo.jmskafka.com.example.demo.jmskafka.config;

import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfiguration {

  @Bean
  public ProducerFactory<String, String> producerFactory(KafkaProperties kafkaProperties) {

    Map<String, Object> properties = kafkaProperties.buildProducerProperties();
    properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
    properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, Long.MAX_VALUE);
    return new DefaultKafkaProducerFactory<>(properties);
  }

  @Bean
  public KafkaTemplate<String, String> getKafkaTemplate(ProducerFactory<String, String> factory) {

    return new KafkaTemplate<>(factory);
  }
}
