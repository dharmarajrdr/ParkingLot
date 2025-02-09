package com.dharmaraj.parking_lot.dtos;


import com.dharmaraj.parking_lot.models.Ticket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateTicketResponseDto {

    private Ticket ticket;
    private ResponseStatus responseStatus;
}
