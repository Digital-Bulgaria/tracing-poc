package com.example.demo.jmskafka.kafka.Message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import net.logstash.logback.encoder.org.apache.commons.lang.builder.EqualsBuilder;
import net.logstash.logback.encoder.org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.cloud.sleuth.Span;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KafkaPayload<T> {

  private final Integer version;
  private Span span;

  @NotNull
  @Valid
  @JsonUnwrapped
  private final T message;

  public KafkaPayload(@JsonProperty("version") final Integer version,
      @JsonProperty("message") final T message, @JsonProperty("span") Span span ) {
    this.version = version;
    this.message = message;
    this.span = span;
  }

  public Integer getVersion() {
    return version;
  }

  public T getMessage() {
    return message;
  }

  public Span getSpan() {
    return span;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() != getClass()) {
      return false;
    }
    KafkaPayload rhs = (KafkaPayload) obj;
    return new EqualsBuilder()
        .append(this.version, rhs.version)
        .append(this.message, rhs.message)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(version)
        .append(message)
        .toHashCode();
  }

  @Override
  public String toString() {

    return "KafkaPayload{" +
        "version=" + version +
        ", message=" + message +
        '}';
  }
}
