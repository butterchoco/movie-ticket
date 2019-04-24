package com.adpro.ticket.api;

import com.adpro.ticket.model.Booking;

import java.util.Optional;

public interface TicketService {
    boolean canOrderTicket(TicketRequestModel r);
    Optional<Booking> orderTicket(TicketRequestModel r);
    Optional<Booking> verifyTicket(Long ticketId);
}
