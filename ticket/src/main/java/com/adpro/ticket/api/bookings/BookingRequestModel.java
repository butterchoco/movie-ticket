package com.adpro.ticket.api.bookings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestModel {
    private Long sessionId;
    private List<String> seatIds;
    private String email;
    private int price;

    public BookingRequestModel(Long sessionId, String seatId, String email, int price) {
        this.sessionId = sessionId;
        this.seatIds = Collections.singletonList(seatId);
        this.email = email;
        this.price = price;
    }
}
