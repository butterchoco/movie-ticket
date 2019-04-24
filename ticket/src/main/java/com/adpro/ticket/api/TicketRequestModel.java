package com.adpro.ticket.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestModel {
    private Long sessionId;
    private List<String> seatIds;

    public TicketRequestModel(Long sessionId, String seatId) {
        this.sessionId = sessionId;
        this.seatIds = Collections.singletonList(seatId);
    }
}
