package com.narwal.bookingservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Document("tickets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
//TODO add fare using baseFare from trip
public class Ticket {
    @Id
    @Indexed
    private String ticketId;
    private String PNR;
    private List<Passenger> passengers;
    private Boolean cancellable;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate journeyDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate bookingDate;
    private String email;
    private String tripScheduleId;
    private HashMap<String, List<Integer>> seats;
    private String status;
    private String fromStationCode;
    private String toStationCode;
    private String mobile;
    private String trainNo;
    private String userId;
}
