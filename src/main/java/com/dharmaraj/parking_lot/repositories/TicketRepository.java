package com.dharmaraj.parking_lot.repositories;

import com.dharmaraj.parking_lot.models.*;

import java.util.Optional;

public interface TicketRepository {
    // Do not modify the method signature, feel free to add new methods

    public Ticket save(Ticket ticket);

    public Optional<Ticket> getTicketById(long ticketId);
}
