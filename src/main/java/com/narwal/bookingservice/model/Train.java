package com.narwal.bookingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Train {
    @Id
    @Indexed
    private String number;
    private String name;
    private String fromStationCode;
    private String toStationCode;
    private int firstAcSeats;
    private int secondAcSeats;
    private int thirdAcSeats;
    private int firstClassSeats;
    private int chairCarSeats;
    private int sleeperSeats;
    private int durationHrs;
    private int durationMns;
    private LocalTime departure;
    private LocalTime arrival;
    private int distance;

    @Override
    public String toString() {
        return "Train{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", fromStationCode='" + fromStationCode + '\'' +
                ", toStationCode='" + toStationCode + '\'' +
                ", firstAcSeats=" + firstAcSeats +
                ", secondAcSeats=" + secondAcSeats +
                ", thirdAcSeats=" + thirdAcSeats +
                ", firstClassSeats=" + firstClassSeats +
                ", chairCarSeats=" + chairCarSeats +
                ", sleeperSeats=" + sleeperSeats +
                ", durationHrs=" + durationHrs +
                ", durationMns=" + durationMns +
                ", departure=" + departure +
                ", arrival=" + arrival +
                ", distance=" + distance +
                '}';
    }
}
