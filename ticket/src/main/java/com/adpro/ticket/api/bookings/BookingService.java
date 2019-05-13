package com.adpro.ticket.api.bookings;

import com.adpro.ticket.model.Booking;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface BookingService {
    boolean canCreateBooking(BookingRequestModel r);
    Optional<Booking> createBooking(BookingRequestModel r);
    Optional<Booking> verifyBooking(Long ticketId);
    CompletableFuture<BookingData> getBookingData(Booking booking);
}
