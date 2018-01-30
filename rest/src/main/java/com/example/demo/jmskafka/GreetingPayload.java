package com.example.demo.jmskafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GreetingPayload {

  private String name;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate date;

  private String message;

  @JsonCreator
  public GreetingPayload(@JsonProperty("name") String name,
                         @JsonProperty("date") LocalDate date,
                         @JsonProperty("message") String message) {

  }
}
