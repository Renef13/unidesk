package br.ufma.glp.unidesk.backend.core.modelmapper;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.AlunoCadastroInput;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
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

        modelMapper.addMappings(new PropertyMap<AlunoCadastroInput, Aluno>() {
            @Override
            protected void configure() {
                skip(destination.getCurso());
            }
        });

        return modelMapper;
    }
}