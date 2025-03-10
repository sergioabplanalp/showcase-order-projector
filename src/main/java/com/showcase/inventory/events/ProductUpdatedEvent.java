package com.showcase.inventory.events;

import java.io.Serializable;

public record ProductUpdatedEvent(
        String id,
        String name,
        String description,
        double price,
        String imageUrl) implements Serializable {
}
