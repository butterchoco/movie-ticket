package com.adpro.ticket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "BOOKING")
@EqualsAndHashCode(exclude = "tickets")
@ToString(exclude = "tickets")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long sessionId;
    private Status status;
    private String email;
    private int price;
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private Set<Ticket> tickets;

    public Booking(Long sessionId, Status status, Set<Ticket> tickets, String email, int price) {
        this.sessionId = sessionId;
        this.status = status;
        this.tickets = tickets;
        this.email = email;
        this.price = price;
        this.tickets.forEach(x -> x.setBooking(this));
    }

    public enum Status {
        PENDING, VERIFIED, CANCELLED
    }


}
