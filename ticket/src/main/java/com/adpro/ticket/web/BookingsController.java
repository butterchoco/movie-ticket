package com.adpro.ticket.web;

import com.adpro.ticket.api.bookings.BookingData;
import com.adpro.ticket.api.bookings.BookingRequestModel;
import com.adpro.ticket.api.bookings.BookingService;
import com.adpro.ticket.api.movies.MovieService;
import com.adpro.ticket.api.notifications.UserNotificationService;
import com.adpro.ticket.model.Booking;
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
    private MovieService movieService;

    public BookingsController(BookingService bookingService, UserNotificationService userNotificationService, MovieService movieService) {
        this.bookingService = bookingService;
        this.userNotificationService = userNotificationService;
        this.movieService = movieService;
    }

    @PostMapping
    @RequestMapping("/bookings")
    public ResponseEntity<Booking> bookings(@RequestBody BookingRequestModel requestTicket) {
        return bookingService.createBooking(requestTicket)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body(null));
    }

    @PostMapping
    @RequestMapping("/bookings/{bookingId}/verify")
    public ResponseEntity<Booking> verify(@PathVariable(name = "bookingId") Long bookingId) {
        var booking = bookingService.verifyBooking(bookingId).orElse(null);

        if (booking == null) {
            return ResponseEntity.badRequest().body(null);
        }

        if (booking.getStatus() == Booking.Status.VERIFIED) {
            movieService.getMovieSessionById(booking.getSessionId())
                    .thenApply(session -> new BookingData(booking, session))
                    .thenCompose(userNotificationService::sendBookingData);
        }

        return ResponseEntity.ok(booking);
    }
}
