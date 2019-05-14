package com.adpro.ticket.api.notifications;

import com.adpro.ticket.api.bookings.BookingData;

import java.util.concurrent.CompletableFuture;

public interface UserNotificationService {
    CompletableFuture<MessageResponse> sendBookingData(BookingData bookingData);
}
