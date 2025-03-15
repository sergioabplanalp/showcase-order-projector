package com.showcase.projection.productlookup;

import com.showcase.inventory.events.ProductCreatedEvent;
import com.showcase.inventory.events.ProductUpdatedEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("productLookup")
public class ProductLookup {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private double price;

    public static ProductLookup from(ProductCreatedEvent event) {
        return ProductLookup.builder()
                .id(event.id())
                .name(event.name())
                .description(event.description())
                .imageUrl(event.imageUrl())
                .price(event.price())
                .build();
    }

    public void update(ProductUpdatedEvent event) {
        this.name = event.name();
        this.description = event.description();
        this.imageUrl = event.imageUrl();
        this.price = event.price();
    }
}
