package com.example.demo.jmskafka.com.example.demo.jmskafka.config;

import javax.jms.ConnectionFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
public class JMSConfiguration {

  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
      DefaultJmsListenerContainerFactoryConfigurer configurer,
      ConnectionFactory connectionFactory) {

    DefaultJmsListenerContainerFactory containerFactory = new DefaultJmsListenerContainerFactory();
    configurer.configure(containerFactory, connectionFactory);
    containerFactory.setErrorHandler(new JMSErrorHandler());
    containerFactory.setBackOff(new ExponentialBackOff());

    return containerFactory;
  }
}
