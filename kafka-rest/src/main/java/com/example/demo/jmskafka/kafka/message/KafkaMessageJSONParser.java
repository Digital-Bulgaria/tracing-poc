package com.example.demo.jmskafka.kafka.message;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

/**
 * Parses JSON messages which are retrieved from Kafka
 */
@Component
public class KafkaMessageJSONParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageJSONParser.class);

  private final ObjectMapper objectMapper;

  @Autowired
  public KafkaMessageJSONParser(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T> Optional<KafkaMessage<T>> parseJsonString(final String message, Class<T> clazz) {

    try {
      JavaType javaType = objectMapper.getTypeFactory()
          .constructParametricType(KafkaMessage.class, clazz);
      KafkaMessage<T> readValue = objectMapper.readValue(message, javaType);
      return Optional.of(readValue);
    } catch (final JsonParseException e) {
      LOGGER.error("Failed to parse message as JSON ('{}')", message, e);
    } catch (final JsonMappingException e) {
      LOGGER.error("Failed to parse message as {} ('{}')", clazz, message, e);
    } catch (final IOException e) {
      LOGGER.error(
          "a low-level I/O problem (unexpected end-of-input, network error) occurred while parsing message from kafka: "
              + "{}", message, e);
      throw new ListenerExecutionFailedException("Unable to process message, maybe will retry...",
          e);
    }
    return Optional.empty();
  }


}
