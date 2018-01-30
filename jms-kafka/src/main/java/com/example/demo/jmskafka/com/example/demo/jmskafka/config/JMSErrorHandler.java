package com.example.demo.jmskafka.com.example.demo.jmskafka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

public class JMSErrorHandler implements ErrorHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(JMSErrorHandler.class);

  @Override
  public void handleError(Throwable t) {

    LOGGER.error("Error in JMS listener", t);
  }
}
