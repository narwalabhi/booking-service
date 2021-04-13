package com.narwal.bookingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Document("tickets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private String userId;
    private String tripScheduleId;
    private HashMap<String, List<Integer>> seats;
    private String status;

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", PNR='" + PNR + '\'' +
                ", passengers=" + passengers +
                ", cancellable=" + cancellable +
                ", journeyDate=" + journeyDate +
                ", userId='" + userId + '\'' +
                ", tripScheduleId='" + tripScheduleId + '\'' +
                ", seats=" + seats +
                ", status='" + status + '\'' +
                '}';
    }
}
