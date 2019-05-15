package com.adpro.ticket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "TICKET")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookingId", updatable = false)
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    private Booking booking;
    private String seatId;

    public Ticket(String seatId) {
        this.seatId = seatId;
    }
}
