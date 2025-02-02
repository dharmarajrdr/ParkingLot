package com.dharmaraj.parking_lot.dtos;


import com.dharmaraj.parking_lot.models.Ticket;

public class GenerateTicketResponseDto {

    private Ticket ticket;
    private ResponseStatus responseStatus;

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}
