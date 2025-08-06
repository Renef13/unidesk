package br.ufma.glp.unidesk.backend.api.v1.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DashboardModel {
    
    Long totalTickets;
    Long totalTicketsResolvidos;
    Long totalTicketsAbertos;
    Long totalTicketsPendentes;
    Long totalTicketsEmAndamento;
    Double porcentagemProgresso;
    Double porcentagemAbertos;
    Double porcentagemResolvidos;
    Double porcentagemAndamento;
}
