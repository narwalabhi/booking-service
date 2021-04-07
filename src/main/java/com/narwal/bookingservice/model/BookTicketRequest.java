package com.narwal.bookingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookTicketRequest {

    private String tripId;
    private Date tripDate;
    private String trainId;
    private HashMap<String, Integer> seats;
    private List<Passenger> passengers;

    @Override
    public String toString() {
        return "BookTicketRequest{" +
                "tripId='" + tripId + '\'' +
                ", tripDate=" + tripDate +
                ", trainId='" + trainId + '\'' +
                ", seats=" + seats +
                ", passengers=" + passengers +
                '}';
    }
}
