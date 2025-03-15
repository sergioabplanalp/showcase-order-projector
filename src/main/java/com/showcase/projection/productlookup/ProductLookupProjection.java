package com.showcase.projection.productlookup;

import com.showcase.inventory.events.ProductCreatedEvent;
import com.showcase.inventory.events.ProductUpdatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class ProductLookupProjection {
    private final ProductLookupRepository productLookupRepository;

    public ProductLookupProjection(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @EventHandler
    public void handle(ProductCreatedEvent event) {
        productLookupRepository.save(ProductLookup.from(event));
    }

    @EventHandler
    public void handle(ProductUpdatedEvent event) {
        ProductLookup productLookup = productLookupRepository.findById(event.id())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        productLookup.update(event);
        productLookupRepository.save(productLookup);
    }
}
