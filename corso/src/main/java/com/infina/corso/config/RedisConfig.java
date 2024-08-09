package com.infina.corso.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infina.corso.model.Currency;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    private final ObjectMapper objectmapper;

    public RedisConfig(ObjectMapper objectMapper) {
        this.objectmapper = objectMapper;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectmapper);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.setDefaultSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }


    @Bean
    public RedisTemplate<String, Currency> currencyRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Currency> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<Currency> serializer = new Jackson2JsonRedisSerializer<>(Currency.class);
        template.setValueSerializer(serializer);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.setDefaultSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }
}
