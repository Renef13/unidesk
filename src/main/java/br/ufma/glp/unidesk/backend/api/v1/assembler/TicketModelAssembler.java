package br.ufma.glp.unidesk.backend.api.v1.assembler;

import br.ufma.glp.unidesk.backend.api.v1.dto.model.TicketModel;
import br.ufma.glp.unidesk.backend.domain.model.Ticket;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TicketModelAssembler {
    private final ModelMapper modelMapper;

    public TicketModel toModel(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        return modelMapper.map(ticket, TicketModel.class);
    }

    public List<TicketModel> toCollectionModel(List<Ticket> tickets) {
        if (tickets == null) {
            return List.of();
        }
        return tickets.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
}
