package com.adpro.ticket.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Currency;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestModel {
    private Long sessionId;
    private List<String> seatIds;
    private String email;
    private int price;

    public TicketRequestModel(Long sessionId, String seatId, String email, int price) {
        this.sessionId = sessionId;
        this.seatIds = Collections.singletonList(seatId);
        this.email = email;
        this.price = price;
    }
}
