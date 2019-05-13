package com.adpro.ticket;

import com.adpro.ticket.api.Movie;
import com.adpro.ticket.api.MovieSession;
import com.adpro.ticket.api.BookingData;
import com.adpro.ticket.model.Booking;
import com.adpro.ticket.model.Ticket;
import org.junit.Assert;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class BookingDataTests {
    @Test
    public void testBookingData() {
        Booking booking = new Booking(1L, Booking.Status.VERIFIED, Set.of(new Ticket("1A")), "ramadistra@gmail.com", 12999);
        Movie movie = Movie.builder()
                .name("Fairuzi Adventures")
                .description("Petualangan seorang Fairuzi")
                .duration(Duration.ofMinutes(111))
                .posterUrl("sdada")
                .releaseDate(LocalDate.now())
                .id(1L)
                .build();
        MovieSession movieSession = new MovieSession(movie, LocalDateTime.now());

        BookingData bookingData = new BookingData(booking, movieSession);

        Assert.assertEquals(booking.getId(), bookingData.getId());
        Assert.assertEquals(booking.getStatus(), bookingData.getStatus());
        Assert.assertEquals(movieSession, bookingData.getMovieSession());
        Assert.assertEquals(booking.getTickets(), bookingData.getTickets());
    }
}
