package com.example.demo.jmskafka.rest;

import com.example.demo.jmskafka.domain.Greeting;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestSender {
  private static final Logger LOGGER = LoggerFactory.getLogger(RestSender.class);

  private RestTemplate restTemplate;
  private Tracer tracer;

  private static final String REST_RESOURCE = "http://localhost:8080/greeting";

  @Autowired
  public RestSender(RestTemplate restTemplate, Tracer tracer) {
    this.restTemplate = restTemplate;
    this.tracer = tracer;
  }

  public void send(Greeting greeting) {
    Objects.requireNonNull(greeting);
    LOGGER.info("We are going to post greeting [{}]",greeting);

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage());
    }

    Greeting postedGreeting = restTemplate
        .postForObject(REST_RESOURCE, new HttpEntity<>(greeting), Greeting.class);

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage());
    }

    Span span =tracer.getCurrentSpan();
    if (span!=null){
      tracer.close(span);
    }
    LOGGER.info("Posted greeting [{}]", postedGreeting);
  }
}
