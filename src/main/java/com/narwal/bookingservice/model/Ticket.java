package com.narwal.bookingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("tickets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ticket {
    @Id
    @Indexed
    private String ticketId;
    private String PNR;
    private List<Passenger> passengers;
    private Boolean cancellable;
    private Date journeyDate;
    private String userId;
    private String tripScheduleId;
}
