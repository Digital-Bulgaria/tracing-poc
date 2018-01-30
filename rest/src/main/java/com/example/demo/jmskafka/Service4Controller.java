package com.example.demo.jmskafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @GetMapping("/senkoExpenses")
  public String calculateExpensesForSenko() {

    LOGGER.info("Launching calculation for Senko's expenses...");

    String expenses = "";

    Span newSpan = this.tracer.createSpan("calculateExpenses");
    try {
      expenses = String.valueOf(600+200);
      // ...
      // You can tag a span
      this.tracer.addTag("senkoExpenses", expenses);
      // ...
      // You can log an event on a span
      newSpan.logEvent("senkoExpensesCalculated");
    } finally {
      // Once done remember to close the span. This will allow collecting
      // the span to send it to Zipkin
      this.tracer.close(newSpan);
    }

    return expenses;
  }

  @PostMapping("/greeting")
  public void receiveGreeting() {


  }
}
