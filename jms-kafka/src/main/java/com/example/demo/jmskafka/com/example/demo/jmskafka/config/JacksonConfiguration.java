package com.example.demo.jmskafka.com.example.demo.jmskafka.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfiguration {

  public static void configure(final ObjectMapper mapper) {

    createObjectMapperBuilder().configure(mapper);
  }

  @SuppressWarnings("unchecked")
  private static Jackson2ObjectMapperBuilder createObjectMapperBuilder() {

    return new Jackson2ObjectMapperBuilder()
        .modulesToInstall(JavaTimeModule.class)
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .featuresToDisable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
        .featuresToDisable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
        .featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
  }

  @Bean
  public Jackson2ObjectMapperBuilder objectMapperBuilder() {

    return createObjectMapperBuilder();
  }
}
