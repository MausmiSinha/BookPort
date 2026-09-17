package com.example.BookPort.common.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;

import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import com.example.BookPort.common.dto.IntraRequest;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
	@Bean
	public ConsumerFactory<String, IntraRequest> consumerFactory() {

	    Map<String, Object> config = new HashMap<>();

	    config.put(
	        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
	        "localhost:9092"
	    );

	    config.put(
	        ConsumerConfig.GROUP_ID_CONFIG,
	        "test-group"
	    );

	    config.put(
	        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
	        StringDeserializer.class
	    );

	    config.put(
	        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
	        JsonDeserializer.class
	    );

	    config.put(
	        JsonDeserializer.TRUSTED_PACKAGES,
	        "*"
	    );

	    return new DefaultKafkaConsumerFactory<>(
	        config,
	        new StringDeserializer(),
	        new JsonDeserializer<>(IntraRequest.class)
	    );
	}
	
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, IntraRequest>
	kafkaListenerContainerFactory() {

	    ConcurrentKafkaListenerContainerFactory<String, IntraRequest> factory =
	        new ConcurrentKafkaListenerContainerFactory<>();

	    factory.setConsumerFactory(consumerFactory());

	    return factory;
	}

}
