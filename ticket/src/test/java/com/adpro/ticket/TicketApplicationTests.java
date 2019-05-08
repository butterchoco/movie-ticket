package com.adpro.ticket;

import com.adpro.ticket.api.MovieService;
import com.adpro.ticket.api.TicketRequestModel;
import com.adpro.ticket.model.Booking;
import com.adpro.ticket.model.Ticket;
import com.adpro.ticket.repository.BookingRepository;
import com.adpro.ticket.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

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
    private TicketRepository ticketRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void testCanOrderSeat() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new TicketRequestModel(1L, "1B"));
        this.mvc.perform(post("/tickets").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(jsonPath("$.status", is("PENDING")))
                .andExpect(status().isOk());
    }

    @Test
    public void testCannotOrderBookedSeat() throws Exception {
        Booking booking = bookingRepository.save(new Booking(1L, Booking.Status.VERIFIED, Set.of(new Ticket("1A"))));
        String json = new ObjectMapper().writeValueAsString(new TicketRequestModel(1L, "1A"));
        this.mvc.perform(post("/tickets").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCanVerifyTicket() throws Exception {
        Booking booking = bookingRepository.save(new Booking(2L, Booking.Status.PENDING, Set.of(new Ticket("1A"))));
        this.mvc.perform(post("/tickets/" + booking.getId() + "/verify"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("VERIFIED")));
    }

    @Test
    public void testCancelledTicketNotVerified() throws Exception {
        Booking booking = bookingRepository.save(new Booking(3L, Booking.Status.CANCELLED, Set.of(new Ticket("1A"))));
        this.mvc.perform(post("/tickets/" + booking.getId() + "/verify"))
                .andExpect(jsonPath("$.status", is("CANCELLED")));
    }

    @Test
    public void testVerifyInvalidTicket() throws Exception {
        this.mvc.perform(post("/tickets/12321/verify"))
                .andExpect(status().is4xxClientError());
    }
}