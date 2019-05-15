package com.adpro.ticket.api.bookings;

import java.io.IOException;

public interface TicketGenerator {
    byte[] generateTicket(BookingData bookingData) throws IOException;
}
