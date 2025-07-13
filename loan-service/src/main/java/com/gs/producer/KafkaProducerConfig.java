package com.gs.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.gs.event.LoanEvent;
@Configuration
public class KafkaProducerConfig {
    
    @Autowired
    private org.springframework.core.env.Environment env;

    @Bean
    public ProducerFactory<String, LoanEvent> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.bootstrap-servers"));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, LoanEvent> kafkaTemplate() {
        ProducerFactory<String, LoanEvent> producerFactory = producerFactory();
        KafkaTemplate<String, LoanEvent> template = new KafkaTemplate<>(producerFactory);
        System.out.println("🔍 Kafka Bootstrap Servers: " + producerFactory.getConfigurationProperties().get("bootstrap.servers"));
        return template;
    }
}