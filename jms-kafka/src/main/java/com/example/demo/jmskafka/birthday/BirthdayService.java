package com.example.demo.jmskafka.birthday;

import com.example.demo.jmskafka.kafka.sender.KafkaSender;
import org.springframework.stereotype.Service;

@Service
public class BirthdayService {

  private KafkaSender kafkaSender;

  public BirthdayService(KafkaSender kafkaSender) {
    this.kafkaSender = kafkaSender;
  }

  public void sendGreeting( BirthdayGreetingMessage birthdayGreetingMessage ){

//    kafkaSender.send();
  }
}
