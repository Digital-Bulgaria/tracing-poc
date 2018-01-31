package com.example.demo.jmskafka.kafka.sender;

import com.example.demo.jmskafka.kafka.KafkaMessage;
import com.example.demo.jmskafka.kafka.KafkaPayload;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

/**
 * Send message to kafka.
 */
@Component("kafka_sender")
public class KafkaSender {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSender.class);

  private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

  private Tracer tracer;

  @Autowired
  public KafkaSender(KafkaTemplate<String, KafkaMessage> kafkaTemplate,
      Tracer tracer) {

    this.kafkaTemplate = kafkaTemplate;
    this.tracer = tracer;
  }

  /**
   * Send message to kafka
   *
   * <p> Example: <p> FooEntity foo = new FooEntity(); KafkaSender.send( "topic", "type",
   * foo.getKey(), 1, foo );
   *
   * @param <T> The type of the entity.
   * @param topic Topic name.
   * @param type The type of the message.
   * @param key The key from the entity.
   * @param payloadVersion The version of the entity.
   * @param payload The entity which will be published in kafka.
   * @param successCallback Called after message was sent successfully
   * @param failureCallback Called in case a message could not be sent
   */
  public <T> void send(final String topic, final String type, final String key,
      final Integer payloadVersion, final T payload, Span span ,
      final SuccessCallback<SendResult<String, KafkaMessage>> successCallback,
      final FailureCallback failureCallback) {

    LOGGER.info("Publishing message {}, {}, {}, {}.", topic, type, payload, span );

    final KafkaPayload<T> kafkaPayload = new KafkaPayload<>(payloadVersion, payload,span);
    final KafkaMessage<T> message = new KafkaMessage<>(UUID.randomUUID(), key,
        ZonedDateTime.now(ZoneOffset.UTC), type, kafkaPayload);
    kafkaTemplate.send(topic, message.getKey(), message)
        .addCallback(successCallback, failureCallback);

  }
}
