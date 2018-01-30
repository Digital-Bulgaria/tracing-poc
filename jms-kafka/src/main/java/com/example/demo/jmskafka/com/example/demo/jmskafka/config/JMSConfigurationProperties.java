package com.example.demo.jmskafka.com.example.demo.jmskafka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "activemq")
public class JMSConfigurationProperties {

  private String destination;

  public String getDestination() {

    return destination;
  }

  public void setDestination(String destination) {

    this.destination = destination;
  }
}
