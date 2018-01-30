package com.example.demo.jmskafka.kafka.Message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * NOTE: THIS IS TAKEN FROM REWE REFERENCE IMPLEMENTATION PLEASE SYNC YOUR CHANGES!
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KafkaMessage<T> {

  @NotNull
  private final UUID id;

  @NotEmpty
  private final String key;
  @NotNull
  private final String type;
  @NotNull
  @Valid
  @JsonDeserialize(using = KafkaPayloadDeserializer.class)
  private final KafkaPayload<T> payload;
  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX")//boh, article-v3 uses this format.
  private ZonedDateTime time;

  public KafkaMessage(@JsonProperty("id") final UUID id, @JsonProperty("key") final String key,
      @JsonProperty("time") final ZonedDateTime time, @JsonProperty("type") final String type,
      @JsonProperty("payload") final KafkaPayload<T> payload) {
    this.id = id;
    this.key = key;
    this.time = time;
    this.type = type;
    this.payload = payload;
  }

  public UUID getId() {
    return id;
  }

  public String getKey() {
    return key;
  }

  public ZonedDateTime getTime() {
    return time;
  }

  public String getType() {
    return type;
  }

  public KafkaPayload<T> getPayload() {
    return payload;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof KafkaMessage)) {
      return false;
    }
    KafkaMessage<?> that = (KafkaMessage<?>) o;
    return Objects.equals(getId(), that.getId()) &&
        Objects.equals(getKey(), that.getKey()) &&
        Objects.equals(getType(), that.getType()) &&
        Objects.equals(getPayload(), that.getPayload()) &&
        Objects.equals(getTime(), that.getTime());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getId(), getKey(), getType(), getPayload(), getTime());
  }

  @Override
  public String toString() {

    return "KafkaMessage{" +
        "id=" + id +
        ", key='" + key + '\'' +
        ", time=" + time +
        ", type='" + type + '\'' +
        ", payload=" + payload +
        '}';
  }
}