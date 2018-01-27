package com.example.demo.jmskafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Service4Controller {

  private static final Logger LOGGER = LoggerFactory.getLogger(Service4Controller.class);

  private final RestTemplate restTemplate;
  private final Tracer tracer;

  public Service4Controller(RestTemplate restTemplate,
      Tracer tracer) {
    this.restTemplate = restTemplate;
    this.tracer = tracer;
  }

  @GetMapping("/readtimeout")
  public String readtimeout() throws InterruptedException {

    LOGGER.info("Service4");
    Thread.sleep(5000);
    return "Finish";
  }
}
