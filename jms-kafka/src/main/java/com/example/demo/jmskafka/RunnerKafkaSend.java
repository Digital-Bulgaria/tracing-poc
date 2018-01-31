package com.example.demo.jmskafka;

import com.example.demo.jmskafka.domain.Greeting;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

@Service
public class RunnerKafkaSend implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(RunnerKafkaSend.class);

  private BirthdayService birthdayService;
  private Tracer tracer;

  public RunnerKafkaSend(BirthdayService birthdayService,
      Tracer tracer) {
    this.birthdayService = birthdayService;
    this.tracer = tracer;
  }

  @Override
  public void run(String... strings) throws Exception {

    Span span = tracer.createSpan("birhtday");

    LOGGER.info("parent: {}.", span);

    Greeting greeting = new Greeting();
    greeting.setTo("senko");
    greeting.setDate(LocalDate.now());
    greeting.setMessage("Happy birthday!");

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      LOGGER.error(e.getMessage());
    }

    this.birthdayService.sendGreeting(greeting, span);

    tracer.close(span);
  }
}
