package com.adpro.ticket.services.bookings;

import com.adpro.ticket.api.bookings.BookingData;
import com.adpro.ticket.api.movies.MovieService;
import com.adpro.ticket.api.bookings.BookingRequestModel;
import com.adpro.ticket.api.bookings.BookingService;
import com.adpro.ticket.model.Booking;
import com.adpro.ticket.model.Ticket;
import com.adpro.ticket.repository.BookingRepository;
import com.adpro.ticket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BookingsServiceImpl implements BookingService {
    private TicketRepository ticketRepository;
    private BookingRepository bookingRepository;
    private MovieService movieService;

    @Autowired
    public BookingsServiceImpl(TicketRepository ticketRepository, BookingRepository bookingRepository,
                               MovieService movieService) {
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
        this.movieService = movieService;
    }

    @Override
    public boolean canCreateBooking(BookingRequestModel r) {
        return ticketRepository
                .findBySessionIdAndSeatIdsAndStatus(r.getSessionId(), r.getSeatIds(), Booking.Status.VERIFIED)
                .isEmpty();
    }

    @Override
    public Optional<Booking> createBooking(BookingRequestModel r) {
        if (canCreateBooking(r)) {
            Set<Ticket> tickets = r.getSeatIds().stream().map(Ticket::new).collect(Collectors.toSet());
            Booking booking = bookingRepository
                    .save(new Booking(r.getSessionId(), Booking.Status.PENDING, tickets, r.getEmail(), r.getPrice()));
            return Optional.of(booking);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Booking> verifyBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);

        if (!bookingOptional.isPresent()) {
            return Optional.empty();
        }
        Booking booking = bookingOptional.get();

        if (booking.getStatus() == Booking.Status.PENDING) {
            booking.setStatus(Booking.Status.VERIFIED);
            return Optional.of(bookingRepository.save(booking));
        }

        return Optional.of(booking);
    }

    @Override
    public CompletableFuture<BookingData> getBookingData(Booking booking) {
        return movieService.getMovieSessionById(booking.getSessionId())
                .thenApply(session -> new BookingData(booking, session));
    }
}
