package org.andrea.springbootdblock.dto.response;

import java.util.UUID;

public record ProductResponse(
        UUID guid,
        String name,
        int quantity
) {
}
