package com.adpro.ticket;

import com.adpro.ticket.api.TicketRequestModel;
import com.adpro.ticket.api.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {
    private TicketRepository ticketRepository;
    private BookingRepository bookingRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, BookingRepository bookingRepository) {
        this.ticketRepository = ticketRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public boolean canOrderTicket(TicketRequestModel r) {
        return ticketRepository.findBySessionIdAndSeatIdsAndStatus(r.getSessionId(), r.getSeatIds(), Booking.Status.VERIFIED).isEmpty();
    }

    @Override
    public Optional<Booking> orderTicket(TicketRequestModel r) {
        if (canOrderTicket(r)) {
            Booking booking = bookingRepository.save(new Booking(r.getSessionId(), Booking.Status.PENDING));
            List<Ticket> tickets = r.getSeatIds().stream()
                    .map(seatId -> new Ticket(booking, seatId))
                    .collect(Collectors.toList());
            ticketRepository.saveAll(tickets);
            return Optional.of(booking);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Booking> verifyTicket(Long bookingId) {
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
}
