package com.example.demo.jmskafka.kafka.config;

import java.util.Collections;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * Kafka consumer configurator
 */
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConsumerConfig {

  public static final String TRACING_POC_GREETINGS = "tracing_poc_greetings";
  public static final String POC_KAFKA_FACTORY = "pocKafkaFactory";

  private static final int NUMBER_OF_TRIES = Integer.MAX_VALUE;


  @Bean(POC_KAFKA_FACTORY)
  public ConcurrentKafkaListenerContainerFactory<Object, Object> createKafkaListenerContainerFactory
      (ConcurrentKafkaListenerContainerFactoryConfigurer configurer,
          ConsumerFactory<Object, Object> consumerFactory) {

    final ConcurrentKafkaListenerContainerFactory<Object, Object> containerFactory = new
        ConcurrentKafkaListenerContainerFactory<>();
    containerFactory.setRetryTemplate(createRetryTemplate());
    containerFactory.setPhase(Integer.MAX_VALUE);
    configurer.configure(containerFactory, consumerFactory);
    return containerFactory;
  }

  /**
   * Creates a retry template as required by the reference implementation.
   */
  private RetryTemplate createRetryTemplate() {

    //as required.
    final RetryTemplate retryTemplate = new RetryTemplate();

    retryTemplate.setRetryPolicy(
        new SimpleRetryPolicy(NUMBER_OF_TRIES,
            Collections.singletonMap(ListenerExecutionFailedException.class, true)));

    retryTemplate.setBackOffPolicy(getBackOffPolicy());
    retryTemplate.setThrowLastExceptionOnExhausted(true);

    return retryTemplate;
  }

  private BackOffPolicy getBackOffPolicy() {
    //as required by the architects
    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setMultiplier(2);
    backOffPolicy.setMaxInterval(30000);
    backOffPolicy.setInitialInterval(100);
    return backOffPolicy;
  }
}
