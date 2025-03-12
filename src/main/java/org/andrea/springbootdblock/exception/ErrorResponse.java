package org.andrea.springbootdblock.exception;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class ErrorResponse {
    Integer code;
    String message;
    ZonedDateTime timestamp;
}
