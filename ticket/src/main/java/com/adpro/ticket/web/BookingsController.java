package com.adpro.ticket.web;

import com.adpro.ticket.api.bookings.BookingRequestModel;
import com.adpro.ticket.api.bookings.BookingService;
import com.adpro.ticket.api.notifications.UserNotificationService;
import com.adpro.ticket.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingsController {

    private BookingService bookingService;
    private UserNotificationService userNotificationService;

    @Autowired
    public BookingsController(BookingService bookingService, UserNotificationService userNotificationService) {
        this.bookingService = bookingService;
        this.userNotificationService = userNotificationService;
    }

    @PostMapping
    @RequestMapping("/tickets")
    public ResponseEntity<Booking> tickets(@RequestBody BookingRequestModel requestTicket) {
        return bookingService.createBooking(requestTicket)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body(null));
    }

    @PostMapping
    @RequestMapping("/tickets/{bookingId}/verify")
    public ResponseEntity<Booking> verify(@PathVariable(name = "bookingId") Long bookingId) throws Exception {
        var ticket = bookingService.verifyBooking(bookingId).orElse(null);

        if (ticket == null) {
            return ResponseEntity.badRequest().body(null);
        }

        if (ticket.getStatus() == Booking.Status.VERIFIED) {
            bookingService.getBookingData(ticket).thenCompose(userNotificationService::sendBookingData);
        }

        return ResponseEntity.ok(ticket);
    }
}
