package com.adpro.ticket.api.bookings;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class VerifyBookingRequest {
    @Email
    @NotNull
    private String email;
}
