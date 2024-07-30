package com.infina.corso.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    @Qualifier("modelMapperForResponse")
    public ModelMapper modelMapperForResponse() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }

    @Bean
    @Qualifier("modelMapperForRequest")
    public ModelMapper modelMapperForRequest() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }
}
