package com.adpro.ticket.api;

import com.adpro.ticket.model.Booking;
import com.adpro.ticket.model.Ticket;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class BookingData {
    private Booking booking;
    private MovieSession movieSession;

    public Long getId() {
        return booking.getId();
    }

    public Booking.Status getStatus() {
        return booking.getStatus();
    }

    public MovieSession getMovieSession() {
        return movieSession;
    }

    public Set<Ticket> getTickets() {
        return booking.getTickets();
    }

    public String getEmail() {
        return booking.getEmail();
    }

    public int getPrice() {
        return booking.getPrice();
    }
}
