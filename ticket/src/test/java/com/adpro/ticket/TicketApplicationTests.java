package com.adpro.ticket;

import com.adpro.ticket.api.bookings.BookingRequestModel;
import com.adpro.ticket.api.movies.Movie;
import com.adpro.ticket.api.movies.MovieService;
import com.adpro.ticket.api.movies.MovieSession;
import com.adpro.ticket.api.notifications.MessageResponse;
import com.adpro.ticket.model.Booking;
import com.adpro.ticket.model.Ticket;
import com.adpro.ticket.repository.BookingRepository;
import com.adpro.ticket.services.email.EmailClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TicketApplicationTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private BookingRepository bookingRepository;
    @MockBean
    private EmailClient mockEmailClient;
    @MockBean
    private MovieService mockMovieService;

    private MovieSession createMovieSession() {
        Movie movie = Movie.builder()
                .name("Fairuzi Adventures")
                .description("Petualangan seorang Fairuzi")
                .duration(Duration.ofMinutes(111))
                .posterUrl("sdada")
                .releaseDate(LocalDate.now())
                .id(1L)
                .build();
        return new MovieSession(movie, LocalDateTime.now());
    }

    private Booking createBooking(long id, Booking.Status status) {
        return bookingRepository.save(new Booking(id, status, Set.of(new Ticket("1A")), "ramadistra@gmail.com", 12222));
    }


    @Test
    public void testCanOrderSeat() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new BookingRequestModel(1L, "1B", "ramadistra@gmail.com", 12222));
        this.mvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(status().isOk());
    }

    @Test
    public void testCannotOrderBookedSeat() throws Exception {
        var booking = createBooking(1L, Booking.Status.VERIFIED);
        String json = new ObjectMapper().writeValueAsString(new BookingRequestModel(1L, "1A", "ramadistra@gmail.com", 12222));
        this.mvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is4xxClientError());
    }



    @Test
    public void testCanVerifyTicket() throws Exception {
        var lock = new CountDownLatch(1);

        Booking booking = createBooking(2L, Booking.Status.PENDING);
        Mockito.when(mockMovieService.getMovieSessionById(Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(createMovieSession()));
        Mockito.when(mockEmailClient.sendEmail(Mockito.any()))
                .thenAnswer(i -> CompletableFuture.supplyAsync(() -> {
                    lock.countDown();
                    return new MessageResponse("1", "Success!");
                }));
        this.mvc.perform(post("/bookings/" + booking.getId() + "/verify"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("VERIFIED")));

        lock.await();
        Mockito.verify(mockEmailClient, Mockito.atLeastOnce()).sendEmail(Mockito.any());
    }

    @Test
    public void testCancelInvalidatedBookings() throws Exception {
        Mockito.when(mockMovieService.getMovieSessionById(Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(createMovieSession()));
        Mockito.when(mockEmailClient.sendEmail(Mockito.any()))
            .thenReturn(CompletableFuture.completedFuture(
                new MessageResponse("1", "Success!")
            ));

        Booking booking1 = createBooking(3, Booking.Status.PENDING);
        Booking booking2 = createBooking(3, Booking.Status.PENDING);
        
        this.mvc.perform(post("/bookings/" + booking1.getId() + "/verify"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("VERIFIED")));
        this.mvc.perform(post("/bookings/" + booking2.getId() + "/verify"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("CANCELLED")));
    }

    @Test
    public void testCancelledTicketNotVerified() throws Exception {
        Booking booking = createBooking(5, Booking.Status.CANCELLED);
        this.mvc.perform(post("/bookings/" + booking.getId() + "/verify"))
                .andExpect(jsonPath("$.status", is("CANCELLED")));
    }

    @Test
    public void testVerifyInvalidTicket() throws Exception {
        this.mvc.perform(post("/bookings/12321/verify"))
                .andExpect(status().is4xxClientError());
    }
}