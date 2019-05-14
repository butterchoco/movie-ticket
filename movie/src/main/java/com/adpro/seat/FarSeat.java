package com.adpro.seat;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class FarSeat extends Seat{

    @Column(name = "Cost")
    private static int cost;

    public FarSeat() {
        super();
        cost = 40000;
        type = "Far";
    }

    public static int getCost() {
        return cost;
    }

    public static void setCost(int cost) {
        cost = cost;
    }

}
