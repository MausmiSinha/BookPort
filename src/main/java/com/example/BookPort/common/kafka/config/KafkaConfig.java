package com.example.BookPort.common.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import com.example.BookPort.common.dto.IntraRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableKafka
public class KafkaConfig {
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Bean
	public ProducerFactory<String, IntraRequest> producerFactory() {

	    Map<String, Object> config = new HashMap<>();

	    config.put(
	        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
	        "localhost:9092"
	    );


	    return new DefaultKafkaProducerFactory<>(
	    		config,
	    		new StringSerializer(),
	            new JsonSerializer<>(objectMapper)
	    );
	}

	@Bean
	public KafkaTemplate<String, IntraRequest> kafkaTemplate() {
	    return new KafkaTemplate<>(producerFactory());
	}
}
