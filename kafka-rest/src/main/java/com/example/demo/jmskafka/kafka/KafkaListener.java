package com.example.demo.jmskafka.kafka;

import static com.example.demo.jmskafka.kafka.config.KafkaConsumerConfig.POC_KAFKA_FACTORY;
import static com.example.demo.jmskafka.kafka.config.KafkaConsumerConfig.TRACING_POC_GREETINGS;

import com.example.demo.jmskafka.domain.Greeting;
import com.example.demo.jmskafka.kafka.Message.KafkaMessage;
import com.example.demo.jmskafka.kafka.Message.KafkaMessageJSONParser;
import com.example.demo.jmskafka.kafka.Message.KafkaPayload;
import com.example.demo.jmskafka.rest.RestSender;
import java.util.Map;
import java.util.Optional;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaListener implements ConsumerSeekAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaListener.class);

  private final KafkaMessageJSONParser jsonParser;
  private final RestSender restSender;

  @Autowired
  public KafkaListener(KafkaMessageJSONParser jsonParser,RestSender restSender) {
    this.jsonParser = jsonParser;
    this.restSender = restSender;
  }

  @org.springframework.kafka.annotation.KafkaListener(
      containerFactory = POC_KAFKA_FACTORY,
      topics = TRACING_POC_GREETINGS,
      id = "kafka-listener"
  )
  public void consumeMessage(final ConsumerRecord<String, String> consumerRecord,
      final Acknowledgment acknowledgment) {

    LOGGER.debug("Received supplier message with key [{}] from Kafka. Position[t/p/o]: [{}/{}/{}]",
        consumerRecord.key(),
        consumerRecord.topic(),
        consumerRecord.partition(),
        consumerRecord.offset());

    String messageStr = consumerRecord.value();

    Optional<KafkaMessage<Greeting>> messageOpt = jsonParser
        .parseJsonString(messageStr, Greeting.class);

    Greeting greeting = messageOpt.map(KafkaMessage::getPayload)
        .map(KafkaPayload::getMessage)
        .orElse(null);

    if (greeting != null) {
      LOGGER.info("We've get the greeting [{}]",greeting);
      restSender.send(greeting);
    } else {
      //can't parse
      LOGGER.error("We could not parse or understand the greeting message that came from Kafka. "
          + "Maybe scan the error logs for more details. "
          + "The message was [{}]. The key [{}]", messageStr, consumerRecord.key());
    }

    acknowledgment.acknowledge();

    LOGGER.debug("Acknowledged: [{}/{}/{}]",
        consumerRecord.topic(),
        consumerRecord.partition(),
        consumerRecord.offset());
  }


  @Override
  public void registerSeekCallback(ConsumerSeekCallback callback) {

  }

  @Override
  public void onPartitionsAssigned(Map<TopicPartition, Long> assignments,
      ConsumerSeekCallback callback) {
    //code here in case that we need to re-consume something.
    // Be veeery careful if you uncomment this one.
    //    for (Map.Entry<TopicPartition, Long> assingment : assignments.entrySet()) {
    //
    //      callback.seek(
    //          assingment.getKey().topic(),
    //          assingment.getKey().partition(),
    //          0);
    //
    //      LOGGER.info("Schedule a complete reread from KAFKA...t/p={}/{}",
    //                  assingment.getKey().topic(),
    //                  assingment.getKey().partition());
    //    }
  }

  @Override
  public void onIdleContainer(Map<TopicPartition, Long> assignments,
      ConsumerSeekCallback callback) {

  }
}
