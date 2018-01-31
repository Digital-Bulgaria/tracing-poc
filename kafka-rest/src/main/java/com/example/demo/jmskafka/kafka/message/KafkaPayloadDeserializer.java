package com.example.demo.jmskafka.kafka.message;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class KafkaPayloadDeserializer extends JsonDeserializer<KafkaPayload<?>> implements
    ContextualDeserializer {
  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPayloadDeserializer.class);

  private static final String FIELD_REVISION = "revision";
  private static final String FIELD_VERSION = "version";
  private static final String FIELD_DATA = "data";
  private static final String FIELD_SPAN = "span";

  private JavaType valueType;

  private ObjectMapper objectMapper;
  private Tracer tracer;

  @Autowired
  public KafkaPayloadDeserializer(ObjectMapper objectMapper,Tracer tracer) {
    this.objectMapper = objectMapper;
    this.tracer = tracer;
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
    JavaType payloadType = property.getType();
    JavaType valueType = payloadType.containedType(0);
    KafkaPayloadDeserializer deserializer = new KafkaPayloadDeserializer(objectMapper, tracer);
    deserializer.valueType = valueType;

    return deserializer;
  }

  @Override
  public KafkaPayload<?> deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext) throws IOException {
    final JsonNode jsonNode = jsonParser.readValueAsTree();
    final JsonNode dataNode = jsonNode.get(FIELD_DATA);
    final JsonNode spanNode = jsonNode.get(FIELD_SPAN);

    String spanString = "";
    if (spanNode != null) {
      spanString = spanNode.toString();
    }
    Object message;
    Integer version;
    Span span;

    if(StringUtils.hasText(spanString)) {
      Span recievedSpan = objectMapper.readValue(spanString, Span.class);
      LOGGER.info("Span recieved: {}",recievedSpan);

      span = tracer.createSpan("s2", recievedSpan);
      LOGGER.info("Traced span : {}", span);
    } else {
      LOGGER.info("Span not recieved");
      span = tracer.createSpan("s2");
      LOGGER.info("Traced our span : {}",span);
    }

    if (dataNode != null) {
      message = objectMapper
          .treeToValue(dataNode, objectMapper.constructType(valueType).getRawClass());
      //This is a hack. We have many buggy messages with version instead of revision...
      JsonNode revisionNode = jsonNode.get(FIELD_REVISION);
      if (revisionNode == null) {
        revisionNode = jsonNode.get(FIELD_VERSION);
      }
      version = revisionNode.intValue();
    } else {
      message = objectMapper
          .treeToValue(jsonNode, objectMapper.constructType(valueType).getRawClass());
      version = jsonNode.get(FIELD_VERSION).intValue();
    }

    return new KafkaPayload<>(version, message, span);
  }

}
