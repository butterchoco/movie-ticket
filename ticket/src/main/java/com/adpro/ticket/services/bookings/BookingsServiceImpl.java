package com.adpro.ticket.services.bookings;

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
import java.util.stream.Collectors;

@Service
public class BookingsServiceImpl implements BookingService {
    private TicketRepository ticketRepository;
    private BookingRepository bookingRepository;

    @Autowired
    public BookingsServiceImpl(TicketRepository ticketRepository,
                               BookingRepository bookingRepository) {
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public boolean canCreateBooking(BookingRequestModel r) {
        return ticketRepository
            .findBySessionIdAndSeatIdsAndStatus(r.getSessionId(), r.getSeatIds(),
                Booking.Status.VERIFIED)
            .isEmpty();
    }

    @Override
    public Optional<Booking> createBooking(BookingRequestModel r) {
        if (canCreateBooking(r)) {
            Set<Ticket> tickets =
                r.getSeatIds().stream().map(Ticket::new).collect(Collectors.toSet());
            Booking booking = bookingRepository
                .save(new Booking(r.getSessionId(), Booking.Status.PENDING, tickets, r.getEmail()
                    , r.getPrice()));
            return Optional.of(booking);
        }
        return Optional.empty();
    }

    private boolean isValid(Booking booking) {
        return ticketRepository
            .findBySessionIdAndSeatIdsAndStatus(
                booking.getSessionId(),
                booking.getTickets().stream().map(Ticket::getSeatId).collect(Collectors.toList()),
                Booking.Status.VERIFIED)
            .isEmpty();
    }

    @Override
    public synchronized Optional<Booking> verifyBooking(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);

        if (!bookingOptional.isPresent()) {
            return Optional.empty();
        }
        Booking booking = bookingOptional.get();

        if (booking.getStatus() != Booking.Status.PENDING) {
            return Optional.of(booking);
        }

        if (isValid(booking)) {
            booking.setStatus(Booking.Status.VERIFIED);
        } else {
            booking.setStatus(Booking.Status.CANCELLED);
        }

        return Optional.of(bookingRepository.save(booking));
    }
}
