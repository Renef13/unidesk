package br.ufma.glp.unidesk.backend.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(AccessLevel.PRIVATE)
                .setMethodAccessLevel(AccessLevel.PRIVATE)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true);

        return modelMapper;
    }
}