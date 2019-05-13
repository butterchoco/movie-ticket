package com.adpro.ticket.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Theater {
    private Integer id;
    private String description;
    private int seatCount;
}
