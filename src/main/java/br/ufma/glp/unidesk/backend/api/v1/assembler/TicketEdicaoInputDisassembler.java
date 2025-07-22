package br.ufma.glp.unidesk.backend.api.v1.assembler;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketEdicaoInputDisassembler {

    private final ModelMapper modelMapper;


}
