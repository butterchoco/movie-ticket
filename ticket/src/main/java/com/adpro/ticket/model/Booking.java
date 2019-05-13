package com.adpro.ticket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Currency;
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

    public enum Status {
        PENDING, VERIFIED, CANCELLED
    }

    public Booking(Long sessionId, Status status, Set<Ticket> tickets, String email, int price) {
        this.sessionId = sessionId;
        this.status = status;
        this.tickets = tickets;
        this.email = email;
        this.price = price;
        this.tickets.forEach(x -> x.setBooking(this));
    }


}
