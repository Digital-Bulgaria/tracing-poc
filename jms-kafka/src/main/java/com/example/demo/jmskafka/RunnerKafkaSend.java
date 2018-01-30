package com.example.demo.jmskafka;

import com.example.demo.jmskafka.domain.Greeting;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

@Service
public class RunnerKafkaSend implements CommandLineRunner {

  private BirthdayService birthdayService;
  private Tracer tracer;

  public RunnerKafkaSend(BirthdayService birthdayService,
      Tracer tracer) {
    this.birthdayService = birthdayService;
    this.tracer = tracer;
  }

  @Override
  public void run(String... strings) throws Exception {

    tracer.createSpan("birhtday");

    Greeting greeting = new Greeting();
    greeting.setDate(LocalDate.now());
    greeting.setMessage("Happy birthday!");

    this.birthdayService.sendGreeting(greeting);
  }
}
