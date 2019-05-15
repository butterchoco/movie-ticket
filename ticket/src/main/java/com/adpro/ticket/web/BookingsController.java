package com.adpro.ticket.web;

import com.adpro.ticket.api.bookings.BookingData;
import com.adpro.ticket.api.bookings.BookingRequestModel;
import com.adpro.ticket.api.bookings.BookingService;
import com.adpro.ticket.api.bookings.VerifyBookingRequest;
import com.adpro.ticket.api.movies.MovieService;
import com.adpro.ticket.api.notifications.UserNotificationService;
import com.adpro.ticket.model.Booking;
import com.adpro.ticket.model.Ticket;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class BookingsController {

    private BookingService bookingService;
    private UserNotificationService userNotificationService;
    private MovieService movieService;

    public BookingsController(BookingService bookingService,
                              UserNotificationService userNotificationService,
                              MovieService movieService) {
        this.bookingService = bookingService;
        this.userNotificationService = userNotificationService;
        this.movieService = movieService;
    }

    @PostMapping
    @RequestMapping(name = "/bookings", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:8080")
    public ResponseEntity<Booking> bookingsJson(@RequestBody @Valid BookingRequestModel bookingRequest) {
        return handleBookings(bookingRequest);

    }

    @PostMapping
    @RequestMapping(name = "/bookings")
    @CrossOrigin(origins = "localhost:8080")
    public ResponseEntity<Booking> bookingsUrlEncoded(@Valid BookingRequestModel bookingRequest) {
        return handleBookings(bookingRequest);
    }

    private ResponseEntity<Booking> handleBookings(BookingRequestModel bookingRequest) {
        return bookingService.createBooking(bookingRequest)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.badRequest().body(null));
    }

    @PostMapping
    @RequestMapping("/bookings/{bookingId}/verify")
    @CrossOrigin(origins = "localhost:8080")
    public ResponseEntity<Booking> verify(@PathVariable(name = "bookingId") Long bookingId,
                                          @Valid VerifyBookingRequest params) {
        var booking = bookingService.verifyBooking(bookingId, params.getEmail()).orElse(null);

        if (booking == null) {
            return ResponseEntity.badRequest().body(null);
        }

        if (booking.getStatus() == Booking.Status.VERIFIED) {
            var ticketIds = booking.getTickets()
                .stream()
                .map(Ticket::getSeatId)
                .collect(Collectors.toList());
            movieService.saveBooking(booking.getSessionId(), ticketIds);
            movieService.getMovieSessionById(booking.getSessionId())
                .thenApply(session -> new BookingData(booking, session))
                .thenCompose(userNotificationService::sendBookingData);
        }

        return ResponseEntity.ok(booking);
    }
}
