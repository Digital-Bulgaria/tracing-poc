package com.example.demo.jmskafka.kafka.Message;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import org.springframework.cloud.sleuth.Span;

public class KafkaPayloadDeserializer extends JsonDeserializer<KafkaPayload<?>> implements
    ContextualDeserializer {

  public static final String FIELD_REVISION = "revision";
  public static final String FIELD_VERSION = "version";
  public static final String FIELD_DATA = "data";
  public static final String FIELD_SPAN = "span";

  private JavaType valueType;

  private ObjectMapper objectMapper;

  public KafkaPayloadDeserializer() {
    objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Override
  public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
      throws JsonMappingException {
    JavaType payloadType = property.getType();
    JavaType valueType = payloadType.containedType(0);
    KafkaPayloadDeserializer deserializer = new KafkaPayloadDeserializer();
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
      spanString = spanNode.asText();
    }
    Object message;
    Integer version;


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

    return new KafkaPayload<>(version, message,objectMapper.readValue(spanString, Span.class));
  }

}
