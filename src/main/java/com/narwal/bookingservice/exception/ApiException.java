package com.narwal.bookingservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.http.HttpStatus;


import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
public class ApiException {
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}
