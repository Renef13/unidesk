package br.ufma.glp.unidesk.backend.core.modelmapper;

import br.ufma.glp.unidesk.backend.api.v1.dto.input.*;
import br.ufma.glp.unidesk.backend.domain.model.*;
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

        modelMapper.addMappings(new PropertyMap<CoordenacaoCadastroInput, Coordenacao>() {
            @Override
            protected void configure() {
                skip(destination.getCurso());
            }
        });

        modelMapper.addMappings(new PropertyMap<CoordenacaoEdicaoInput, Coordenacao>() {
            @Override
            protected void configure() {
                skip(destination.getCurso());
            }
        });

        modelMapper.addMappings(new PropertyMap<MensagemCadastroInput, Mensagem>() {
            @Override
            protected void configure() {
                skip(destination.getIdMensagem());
                skip(destination.getTicket());
                skip(destination.getUsuario());
            }
        });

        modelMapper.addMappings(new PropertyMap<MensagemEdicaoInput, Mensagem>() {
            @Override
            protected void configure() {
                skip(destination.getIdMensagem());
                skip(destination.getTicket());
                skip(destination.getUsuario());
            }
        });

        modelMapper.addMappings(new PropertyMap<TicketCadastroInput, Ticket>() {
            @Override
            protected void configure() {
                skip(destination.getIdTicket());
                skip(destination.getCoordenacao());
                skip(destination.getFuncionario());
                skip(destination.getAluno());
                skip(destination.getStatus());
                skip(destination.getPrioridade());
                skip(destination.getCategoria());
            }
        });

        modelMapper.addMappings(new PropertyMap<TicketEdicaoInput, Ticket>() {
            @Override
            protected void configure() {
                skip(destination.getIdTicket());
                skip(destination.getCoordenacao());
                skip(destination.getFuncionario());
                skip(destination.getAluno());
                skip(destination.getStatus());
                skip(destination.getPrioridade());
                skip(destination.getCategoria());
            }
        });

        return modelMapper;
    }
}