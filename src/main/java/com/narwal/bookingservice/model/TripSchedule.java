package com.narwal.bookingservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TripSchedule {
    private String id;
    @DateTimeFormat(pattern = "yyyy-mm-dd", iso = DateTimeFormat.ISO.NONE)
    private LocalDate tripDate;
    private int firstAcAvailableSeats;
    private int secondAcAvailableSeats;
    private int thirdAcAvailableSeats;
    private int FirstClassAcAvailableSeats;
    private int chairCarAcAvailableSeats;
    private int SleeperAvailableSeats;
    private String tripId;
    private String trainNo;
    private String status;


    public TripSchedule(LocalDate tripDate, int firstAcAvailableSeats, int secondAcAvailableSeats, int thirdAcAvailableSeats, int firstClassAcAvailableSeats, int chairCarAcAvailableSeats, int sleeperAvailableSeats, String tripId, String trainNo , String status) {
        this.tripDate = tripDate;
        this.firstAcAvailableSeats = firstAcAvailableSeats;
        this.secondAcAvailableSeats = secondAcAvailableSeats;
        this.thirdAcAvailableSeats = thirdAcAvailableSeats;
        FirstClassAcAvailableSeats = firstClassAcAvailableSeats;
        this.chairCarAcAvailableSeats = chairCarAcAvailableSeats;
        SleeperAvailableSeats = sleeperAvailableSeats;
        this.tripId = tripId;
        this.trainNo = trainNo;
        this.status = status;
    }
}
