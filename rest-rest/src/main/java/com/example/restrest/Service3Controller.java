package com.example.restrest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Service3Controller {

  private static final Logger LOGGER = LoggerFactory.getLogger(Service3Controller.class);

  private final RestTemplate restTemplate;
  private final Tracer tracer;

  public Service3Controller(RestTemplate restTemplate,
      Tracer tracer) {
    this.restTemplate = restTemplate;
    this.tracer = tracer;
  }

  @GetMapping("/call-sync-service4")
  public String callSyncService4() throws InterruptedException {

    LOGGER.info("New Span");
    String service4 = restTemplate.getForObject("http://localhost:8080/readtimeout", String.class);
    return String.format("Hello from service2, response from service4 [%s]", service4);
  }

  @GetMapping("/call-error-service4")
  public String callErrorService4() throws InterruptedException {

    // TODO
    return null;
  }

  @GetMapping("/call-async-service4")
  public String callAsyncService4() throws InterruptedException {

    // TODO
    return null;
  }

  @GetMapping("/span-items-service4")
  public String addItemsToSpan() throws InterruptedException {

    // TODO
    LOGGER.info(
        "Service3: Baggage for [key] is [" + tracer.getCurrentSpan().getBaggageItem("key") + "]");
    LOGGER.info("Hello from service2. Calling service3 and then service4");
//    String service3 = restTemplate.getForObject("http://" + serviceAddress3 + "/bar", String.class);
//    LOGGER.info("Got response from service3 [{}]", service3);
//    return String.format("Hello from service2, response from service3 [%s] and from service4 [%s]", service3, service4);
    return "test";
  }

}
