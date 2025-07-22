package br.ufma.glp.unidesk.backend.core.modelmapper;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.AlunoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.AlunoEdicaoInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.BaseConhecimentoCadastroInput;
import br.ufma.glp.unidesk.backend.api.v1.dto.input.BaseConhecimentoEdicaoInput;
import br.ufma.glp.unidesk.backend.domain.model.Aluno;
import br.ufma.glp.unidesk.backend.domain.model.BaseConhecimento;
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
        modelMapper.addMappings(new PropertyMap<AlunoEdicaoInput, Aluno>() {
            @Override
            protected void configure() {
                skip(destination.getCurso());
            }
        });


        modelMapper.addMappings(new PropertyMap<BaseConhecimentoCadastroInput, BaseConhecimento>() {
            @Override
            protected void configure() {
                skip(destination.getCategoria());
            }

        });

        modelMapper.addMappings(new PropertyMap<BaseConhecimentoEdicaoInput, BaseConhecimento>() {
            @Override
            protected void configure() {
                skip(destination.getCategoria());
            }
        });

        return modelMapper;
    }
}