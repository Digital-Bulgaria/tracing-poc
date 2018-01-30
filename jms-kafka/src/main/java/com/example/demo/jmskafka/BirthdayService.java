package com.example.demo.jmskafka;

import com.example.demo.jmskafka.domain.Greeting;
import com.example.demo.jmskafka.kafka.KafkaMessage;
import com.example.demo.jmskafka.kafka.sender.KafkaSender;
import java.util.UUID;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

@Service
public class BirthdayService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BirthdayService.class);
  private final static String TOPIC = "birthday";
  private final static String TYPE = "type";
  private KafkaSender kafkaSender;

  protected final FailureCallback failureCallback =
      (ex) -> {
        LOGGER.error("Failed to send message to Kafka topic", ex);
      };

  protected final SuccessCallback<SendResult<String, KafkaMessage>> successCallback =
      (SendResult<String, KafkaMessage> result) -> {
        ProducerRecord<String, KafkaMessage> record = result.getProducerRecord();
        LOGGER.debug("Successfully sent on topic '{}' message id: {}", record.topic(),
            record.value().getId());
      };

  public BirthdayService(KafkaSender kafkaSender) {
    this.kafkaSender = kafkaSender;
  }

  public void sendGreeting(Greeting greeting) {
    kafkaSender.send(TOPIC, TYPE, UUID.randomUUID().toString(), 1, greeting, successCallback,
        failureCallback);

  }
}
