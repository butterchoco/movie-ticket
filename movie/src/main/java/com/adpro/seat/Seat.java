package com.adpro.seat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Seat")
public class Seat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Seat")
    private Integer seatNumber;

    @Column(name = "Seat_Type")
    public String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TheatreId", updatable = false)
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    private Theatre theatre;

    public Seat() {
        this.seatNumber = seatNumber;
    }

    public Integer getSeatNumber() {
        return this.seatNumber;
    }

    public String getType() {
        return this.type;
    }

    public Theatre getTheatre() {
        return theatre;
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }
}
