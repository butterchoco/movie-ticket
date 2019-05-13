package com.adpro.ticket.api;

import java.util.concurrent.CompletableFuture;

public interface UserNotificationService {
    CompletableFuture<MessageResponse> sendBookingData(BookingData bookingData);
}
