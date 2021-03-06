package com.adpro.ticket.api.bookings;

import com.adpro.ticket.model.Booking;

import java.util.Optional;

public interface BookingService {
    boolean canCreateBooking(BookingRequestModel r);

    Optional<Booking> createBooking(BookingRequestModel r);

    Optional<Booking> verifyBooking(Long ticketId, String email);
}
