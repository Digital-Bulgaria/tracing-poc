package com.example.demo.jmskafka.com.example.demo.jmskafka.kafka;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
/**
 * NOTE: THIS IS TAKEN FROM REWE REFERENCE IMPLEMENTATION PLEASE SYNC YOUR CHANGES!
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KafkaPayload<T> {

  private final Integer version;

  @NotNull
  @Valid
  @JsonUnwrapped
  private final T message;

  public KafkaPayload(@JsonProperty("version") final Integer version,
      @JsonProperty("message") final T message) {

    this.version = version;
    this.message = message;

  }

  public Integer getVersion() {

    return version;
  }

  public T getMessage() {

    return message;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) {
      return true;
    }
    if (!(o instanceof KafkaPayload)) {
      return false;
    }
    KafkaPayload<?> that = (KafkaPayload<?>) o;
    return Objects.equals(getVersion(), that.getVersion()) &&
        Objects.equals(getMessage(), that.getMessage());
  }

  @Override
  public int hashCode() {

    return Objects.hash(getVersion(), getMessage());
  }

  @Override
  public String toString() {

    return "KafkaPayload{" +
        "version=" + version +
        ", message=" + message +
        '}';
  }
}