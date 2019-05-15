package com.adpro.seat;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Seat")
public class Seat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Seat")
    private Integer seatNumber;

    @Column(name = "Bookable")
    private boolean isBooked;

    @Column(name = "Seat_Type")
    public String type;

    public Seat(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public Seat() {
    }

    public Integer getSeatNumber() {
        return this.seatNumber;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void booked() {
        this.isBooked = true;
    }

    public void unbooked() {
        this.isBooked = false;
    }

    public String getType() {
        return this.type;
    }
}
