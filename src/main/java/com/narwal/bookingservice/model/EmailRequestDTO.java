package com.narwal.bookingservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequestDTO {
    private String from;
    private String to;
    private String subject;
    private String name;
}
