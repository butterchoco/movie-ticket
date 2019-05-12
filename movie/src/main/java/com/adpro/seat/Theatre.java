package com.adpro.seat;

import lombok.Data;
import org.hibernate.annotations.NaturalId;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Theatre")
public class Theatre implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "Description")
    private String description;

    @Column(name = "Seat_Count")
    private int seatCount;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @ElementCollection(targetClass=Seat.class)
    private List<Seat> rows;

    public Theatre(String description, int seatCount) {
        this.description = description;
        this.rows = new ArrayList<Seat>();
        this.seatCount = seatCount;
    }

    public Theatre() {}

    public void createRows() {
        for (int seatNum = 1; seatNum <= seatCount; seatNum++) {
            Seat oneSeat;
            if (seatNum < seatCount*2/3) {
                oneSeat = new MiddleSeat(false);
            }
            else {
                oneSeat = new FarSeat(false);
            }
            addSeatToRow(oneSeat);
        }
    }

    public void addSeatToRow(Seat seat) {
        rows.add(seat);
    }

    public String getDescription() {
        return description;
    }

    public int getSeatCount() {
        return seatCount;
    }

    public List<Seat> getRows() {
        return rows;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
