package com.dharmaraj.parking_lot.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.dharmaraj.parking_lot.models.Ticket;

public class TicketRepositoryImpl implements TicketRepository {

    private static final Map<Long, Ticket> allTickets = new HashMap<>();

    @Override
    public Ticket save(Ticket ticket) {
        allTickets.put(ticket.getId(), ticket);
        return ticket;
    }

    @Override
    public Optional<Ticket> getTicketById(long ticketId) {
        return allTickets.containsKey(ticketId) ? Optional.of(allTickets.get(ticketId)) : Optional.empty();
    }
    
}
