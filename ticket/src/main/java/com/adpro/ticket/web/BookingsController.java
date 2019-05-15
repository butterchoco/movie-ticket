package com.adpro.ticket.web;

import com.adpro.ticket.api.bookings.BookingData;
import com.adpro.ticket.api.bookings.BookingRequestModel;
import com.adpro.ticket.api.bookings.BookingService;
import com.adpro.ticket.api.movies.MovieService;
import com.adpro.ticket.api.notifications.UserNotificationService;
import com.adpro.ticket.model.Booking;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(name = "/bookings", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Booking> bookingsJson(@RequestBody BookingRequestModel bookingRequest) {
        return handleBookings(bookingRequest);

    }

    @PostMapping
    @RequestMapping(name = "/bookings")
    public ResponseEntity<Booking> bookingsUrlEncoded(BookingRequestModel bookingRequest) {
        return handleBookings(bookingRequest);
    }

    private ResponseEntity<Booking> handleBookings(BookingRequestModel bookingRequest) {
        return bookingService.createBooking(bookingRequest)
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
