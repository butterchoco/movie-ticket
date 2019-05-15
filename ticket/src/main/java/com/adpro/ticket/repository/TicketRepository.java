package com.adpro.ticket.repository;

import com.adpro.ticket.model.Booking;
import com.adpro.ticket.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t , Booking b WHERE b.sessionId=:sessionId AND t.booking.id=b.id" +
        " AND t.seatId IN :seatIds AND b.status=:status")
    List<Ticket> findBySessionIdAndSeatIdsAndStatus(
        @Param("sessionId") Long sessionId,
        @Param("seatIds") List<String> seatIds,
        @Param("status") Booking.Status status);
}
