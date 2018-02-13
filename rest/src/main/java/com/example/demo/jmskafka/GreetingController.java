package com.example.demo.jmskafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

  private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);

  @PostMapping("/greeting")
  public void receiveGreeting(@RequestBody GreetingPayload greeting) {

    LOGGER.info("Just recived one greeting [{}]!", greeting);
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage());
    }

    LOGGER.info("Just recived one greeting [{}]!", greeting);
  }
}
