package org.andrea.springbootdblock.dto.response;

import java.time.ZonedDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID guid,
        String name,
        int quantity,
        ZonedDateTime  createdAt,
        ZonedDateTime updatedAt
) {
}
