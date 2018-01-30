package com.example.demo.jmskafka;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GreetingPayload {

  @JsonProperty("to")
  private String name;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  private String message;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "GreetingPayload{" +
        "name='" + name + '\'' +
        ", date=" + date +
        ", message='" + message + '\'' +
        '}';
  }
}
