package com.adpro.seat;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Data
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class MiddleSeat extends Seat{

    @Column(name = "Cost")
    private static int cost;

    public MiddleSeat(boolean isBooked) {
        super(isBooked);
        cost = 35000;
        type = "Middle";
    }

    public MiddleSeat() {}

    public static int getCost() {
        return cost;
    }

    public static void setCost(int cost) {
        cost = cost;
    }

}
