package com.adpro.seat;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Data
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class FarSeat extends Seat{

    @Column(name = "Cost")
    private static int cost;

    public FarSeat(boolean isBooked) {
        super(isBooked);
        cost = 40000;
        type = "Far";
    }

    public FarSeat() {}

    public static int getCost() {
        return cost;
    }

    public static void setCost(int cost) {
        cost = cost;
    }

}
