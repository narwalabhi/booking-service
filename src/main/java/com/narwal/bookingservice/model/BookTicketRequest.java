package com.narwal.bookingservice.model;

import lombok.*;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookTicketRequest {

    private String tripId;
    private String tripDate;
    private String trainId;
    private HashMap<String, Integer> seats;
    private List<Passenger> passengers;
    private String email;
    private String mobile;
    private String userId;

}
