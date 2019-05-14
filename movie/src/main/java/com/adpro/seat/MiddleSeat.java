package com.adpro.seat;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class MiddleSeat extends Seat{

    @Column(name = "Cost")
    private static int cost;

    public MiddleSeat() {
        super();
        cost = 35000;
        type = "Middle";
    }

    public static int getCost() {
        return cost;
    }

    public static void setCost(int cost) {
        cost = cost;
    }

}
