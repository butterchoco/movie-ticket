package com.adpro.ticket.web;

import com.adpro.ticket.api.BookingData;
import com.adpro.ticket.api.Movie;
import com.adpro.ticket.api.MovieSession;
import com.adpro.ticket.api.TicketRequestModel;
import com.adpro.ticket.api.TicketService;
import com.adpro.ticket.api.UserNotificationService;
import com.adpro.ticket.model.Booking;
import com.adpro.ticket.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
public class TicketsController {

    private TicketService ticketService;
    private UserNotificationService userNotificationService;

    @Autowired
    public TicketsController(TicketService ticketService, UserNotificationService userNotificationService) {
        this.ticketService = ticketService;
        this.userNotificationService = userNotificationService;
    }

    @PostMapping
    @RequestMapping("/tickets")
    public ResponseEntity<Booking> tickets(@RequestBody TicketRequestModel requestTicket) {
        return ticketService.orderTicket(requestTicket)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body(null));
    }

    @PostMapping
    @RequestMapping("/tickets/{ticketId}/verify")
    public ResponseEntity<Booking> verify(@PathVariable(name = "ticketId") Long ticketId) throws Exception {
        var ticket = ticketService.verifyTicket(ticketId).orElse(null);

        if (ticket == null) {
            return ResponseEntity.badRequest().body(null);
        }

        if (ticket.getStatus() == Booking.Status.VERIFIED) {
            ticketService.getBookingData(ticket).thenAcceptAsync(userNotificationService::sendBookingData);
        }

        return ResponseEntity.ok(ticket);
    }
}
