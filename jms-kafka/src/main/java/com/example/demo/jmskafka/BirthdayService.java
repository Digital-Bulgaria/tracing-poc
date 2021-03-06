package com.example.demo.jmskafka;

import com.example.demo.jmskafka.domain.Greeting;
import com.example.demo.jmskafka.kafka.KafkaMessage;
import com.example.demo.jmskafka.kafka.sender.KafkaSender;
import java.util.UUID;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.Span;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

@Service
public class BirthdayService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BirthdayService.class);
  private final static String TOPIC = "tracing_poc_greetings";
  private final static String TYPE = "type";
  private KafkaSender kafkaSender;

  private final FailureCallback failureCallback =
      (ex) -> LOGGER.error("Failed to send message to Kafka topic", ex);

  private final SuccessCallback<SendResult<String, KafkaMessage>> successCallback =
      (SendResult<String, KafkaMessage> result) -> {
        ProducerRecord<String, KafkaMessage> record = result.getProducerRecord();
        LOGGER.debug("Successfully sent on topic '{}' message id: {}", record.topic(),
            record.value().getId());
      };

  public BirthdayService(KafkaSender kafkaSender) {
    this.kafkaSender = kafkaSender;
  }

  public void sendGreeting(Greeting greeting, Span span) {
    kafkaSender.send(TOPIC, TYPE, UUID.randomUUID().toString(), 1, greeting, span,
        successCallback,
        failureCallback);
  }
}
