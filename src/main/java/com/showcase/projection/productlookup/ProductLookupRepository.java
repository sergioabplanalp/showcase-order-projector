package com.showcase.projection.productlookup;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLookupRepository extends MongoRepository<ProductLookup, String> {
}
