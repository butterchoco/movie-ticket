package com.adpro.ticket.api.bookings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestModel {
    @NotNull
    private Long sessionId;
    @NotNull
    private List<String> seatIds;
    @NotNull
    @Email
    private String email;
    @NotNull
    private int price;

    public BookingRequestModel(Long sessionId, String seatId, String email, int price) {
        this.sessionId = sessionId;
        this.seatIds = Collections.singletonList(seatId);
        this.email = email;
        this.price = price;
    }
}
