package com.narwal.bookingservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("stations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Station {
    @Id
    private String id;
    @Indexed
    private String code;
    private String name;
    private String zone;
    private String state;
}
